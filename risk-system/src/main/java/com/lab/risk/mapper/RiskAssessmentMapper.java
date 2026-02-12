package com.lab.risk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.risk.entity.RiskAssessment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 风险评估记录 Mapper 接口
 */
@Mapper
public interface RiskAssessmentMapper extends BaseMapper<RiskAssessment> {

    /**
     * 分页查询评估记录
     * @param page 分页对象
     * @param projectId 项目ID
     * @param assessmentType 评估类型
     * @param riskLevel 风险等级
     * @return 分页结果
     */
    IPage<RiskAssessment> selectAssessmentPage(
            Page<RiskAssessment> page,
            @Param("projectId") Long projectId,
            @Param("assessmentType") String assessmentType,
            @Param("riskLevel") Integer riskLevel
    );

    /**
     * 查询项目的评估历史
     * @param projectId 项目ID
     * @return 评估历史列表
     */
    @Select("SELECT * FROM t_risk_assessment WHERE project_id = #{projectId} " +
            "ORDER BY assessed_time DESC")
    List<RiskAssessment> selectByProjectId(@Param("projectId") Long projectId);

    /**
     * 查询项目的最新评估记录
     * @param projectId 项目ID
     * @return 最新评估记录
     */
    @Select("SELECT * FROM t_risk_assessment WHERE project_id = #{projectId} " +
            "ORDER BY assessed_time DESC LIMIT 1")
    RiskAssessment selectLatestByProjectId(@Param("projectId") Long projectId);

    /**
     * 根据项目编号查询评估记录
     * @param projectCode 项目编号
     * @return 评估记录列表
     */
    @Select("SELECT * FROM t_risk_assessment WHERE project_code = #{projectCode} " +
            "ORDER BY assessed_time DESC")
    List<RiskAssessment> selectByProjectCode(@Param("projectCode") String projectCode);

    /**
     * 查询指定时间范围的评估记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 评估记录列表
     */
    @Select("SELECT * FROM t_risk_assessment WHERE assessed_time >= #{startTime} " +
            "AND assessed_time <= #{endTime} ORDER BY assessed_time DESC")
    List<RiskAssessment> selectByTimeRange(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    /**
     * 统计各风险等级评估数量
     * @return 统计���果
     */
    @Select("SELECT risk_level, COUNT(*) as count FROM t_risk_assessment " +
            "GROUP BY risk_level")
    List<Map<String, Object>> countByRiskLevel();

    /**
     * 统计各评估类型数量
     * @return 统计结果
     */
    @Select("SELECT assessment_type, COUNT(*) as count FROM t_risk_assessment " +
            "GROUP BY assessment_type")
    List<Map<String, Object>> countByAssessmentType();

    /**
     * 查询平均风险分数
     * @return 平均分数
     */
    @Select("SELECT AVG(risk_score) FROM t_risk_assessment")
    Double selectAvgRiskScore();

    /**
     * 查询最高风险分数
     * @return 最高分数
     */
    @Select("SELECT MAX(risk_score) FROM t_risk_assessment")
    Integer selectMaxRiskScore();

    /**
     * 查询触发特定规则的评估记录
     * @param ruleName 规则名称
     * @return 评估记录列表
     */
    @Select("SELECT * FROM t_risk_assessment WHERE triggered_rules LIKE CONCAT('%', #{ruleName}, '%') " +
            "ORDER BY assessed_time DESC")
    List<RiskAssessment> selectByTriggeredRule(@Param("ruleName") String ruleName);

    /**
     * 统计今日评估数量
     * @return 今日评估数量
     */
    @Select("SELECT COUNT(*) FROM t_risk_assessment WHERE DATE(assessed_time) = CURDATE()")
    Integer countTodayAssessments();

    /**
     * 统计本周评估数量
     * @return 本周评估数量
     */
    @Select("SELECT COUNT(*) FROM t_risk_assessment WHERE YEARWEEK(assessed_time) = YEARWEEK(NOW())")
    Integer countThisWeekAssessments();

    /**
     * 统计本月评估数量
     * @return 本月评估数量
     */
    @Select("SELECT COUNT(*) FROM t_risk_assessment WHERE YEAR(assessed_time) = YEAR(NOW()) " +
            "AND MONTH(assessed_time) = MONTH(NOW())")
    Integer countThisMonthAssessments();

    /**
     * 查询每日评估统计(近7天)
     * @return 统计结果
     */
    @Select("SELECT DATE(assessed_time) as date, COUNT(*) as count " +
            "FROM t_risk_assessment " +
            "WHERE assessed_time >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
            "GROUP BY DATE(assessed_time) " +
            "ORDER BY date ASC")
    List<Map<String, Object>> selectDailyStats();

    /**
     * 查询触发次数最多的规则TOP10
     * @return 统计结果
     */
    List<Map<String, Object>> selectTopTriggeredRules(@Param("limit") Integer limit);

    /**
     * 根据审批层级统计
     * @return 统计结果
     */
    @Select("SELECT required_approval_level, COUNT(*) as count FROM t_risk_assessment " +
            "GROUP BY required_approval_level ORDER BY required_approval_level ASC")
    List<Map<String, Object>> countByApprovalLevel();
}