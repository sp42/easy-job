package com.rdpaas.task.repository;

import com.rdpaas.task.common.Node;
import com.rdpaas.task.common.NotifyCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 任务对象数据库操作对象
 */
@Component
public class NodeRepository {

    @Autowired
    @Qualifier("easyjobJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public long insert(Node node) {
        String sql = "INSERT INTO easy_job_node(node_id,row_num,weight,notify_cmd,create_time,update_time) VALUES (?, ?, ?, ?, ?, ?);";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con)
                    throws SQLException {
                //设置返回的主键字段名
                PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
                ps.setLong(1, node.getNodeId());
                ps.setLong(2, node.getRownum());
                ps.setInt(3, node.getWeight());
                ps.setInt(4, node.getNotifyCmd().getId());
                ps.setTimestamp(5, new java.sql.Timestamp(node.getCreateTime().getTime()));
                ps.setTimestamp(6, new java.sql.Timestamp(node.getUpdateTime().getTime()));

                return ps;
            }
        }, kh);
        return kh.getKey().longValue();
    }

    /**
     * 更新节点心跳时间和序号
     *
     * @param nodeId 待更新节点ID
     */
    public int updateHeartBeat(Long nodeId) {
        StringBuilder sb = new StringBuilder();
        sb.append("update easy_job_node set update_time = now(),row_num = (select tmp.rownum from (")
                .append("SELECT (@i:=@i+1) rownum,node_id FROM `easy_job_node`,(SELECT @i:=0) as rownum where status = 1) tmp where tmp.node_id = ?)")
                .append("where node_id = ?");
        Object objs[] = {nodeId, nodeId};

        return jdbcTemplate.update(sb.toString(), objs);
    }

    /**
     * 更新节点的通知信息,实现修改任务，停止任务通知等
     *
     * @param cmd         通知指令
     * @param notifyValue 通知的值，一般存id
     * @return
     */
    public int updateNotifyInfo(Long nodeId, NotifyCmd cmd, String notifyValue) {
        StringBuilder sb = new StringBuilder();
        sb.append("update easy_job_node set notify_cmd = ?,notify_value = ? where node_id = ?");
        List<Object> objList = new ArrayList<>();
        objList.add(cmd.getId());
        objList.add(notifyValue);

        return jdbcTemplate.update(sb.toString(), objList.toArray());
    }

    /**
     * 当通知执行完后使用乐观锁重置通知信息
     *
     * @param cmd
     * @return
     */
    public int resetNotifyInfo(Long nodeId, NotifyCmd cmd) {
        StringBuilder sb = new StringBuilder();
        sb.append("update easy_job_node set notify_cmd = ?,notify_value = ? ");
        sb.append("where notify_cmd = ? and node_id = ?");
        List<Object> objList = new ArrayList<>();
        objList.add(NotifyCmd.NO_NOTIFY);
        objList.add("");
        objList.add(cmd.getId());
        objList.add(nodeId);
        return jdbcTemplate.update(sb.toString(), objList.toArray());
    }

    /**
     * 禁用节点
     *
     * @param node
     * @return
     */
    public int disbale(Node node) {
        StringBuilder sb = new StringBuilder();
        sb.append("update easy_job_node set status = 0 ")
                .append("where node_id = ?");
        Object objs[] = {node.getNodeId()};
        return jdbcTemplate.update(sb.toString(), objs);
    }

    public List<Node> getEnableNodes(int timeout) {
        StringBuilder sb = new StringBuilder();
        sb.append("select id,node_id as nodeId,row_num as rownum,counts,weight,status,notify_cmd as notifyCmd,notify_value as notifyValue,create_time as createTime,update_time as updateTime from easy_job_node n  ")
                .append("where n.update_time > date_sub(now(), interval ? second) order by node_id");
        Object args[] = {timeout};
        return jdbcTemplate.query(sb.toString(), args, new BeanPropertyRowMapper(Node.class));
    }

    public Node getByNodeId(Long nodeId) {
        String sql = "select id,node_id as nodeId,row_num as rownum,counts,weight,status,notify_cmd as notifyCmd,notify_value as notifyValue,create_time as createTime,update_time as updateTime from easy_job_node where node_id = ?";
        Object objs[] = {nodeId};
        try {
            return (Node) jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper(Node.class), objs);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public long getNextRownum() {
        String sql = "select ifnull(max(row_num),0) + 1 as rownum from easy_job_node";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

}
