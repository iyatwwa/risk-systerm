package com.lab.risk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.risk.entity.ExperimentProject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 实验项目 Mapper 接口
 */
@Mapper
public interface ExperimentProjectMapper extends BaseMapper<ExperimentProject> {

    /**
     * 分页查询实验项目(带条件)
     * @param page 分页对象
     * @param projectName 项目名称(模糊查询)
     * @param experimentType 实验类型
     * @param riskLevel 风险等级
     * @param status 状态
     * @return 分页结果
     */
    IPage<ExperimentProject> selectProjectPage(
            Page<ExperimentProject> page,
            @Param("projectName") String projectName,
            @Param("experimentType") Integer experimentType,
            @Param("riskLevel") String riskLevel,
            @Param("status") String status
    );

    /**
     * 根据项目编号查询
     * @param projectCode 项目编号
     * @return 项目信息
     */
    @Select("SELECT * FROM t_experiment_project WHERE project_code = #{projectCode} AND deleted = 0")
    ExperimentProject selectByProjectCode(@Param("projectCode") String projectCode);

    /**
     * 根据申请人ID查询项目列表
     * @param applicantId 申请人ID
     * @return 项目列表
     */
    @Select("SELECT * FROM t_experiment_project WHERE applicant_id = #{applicantId} AND deleted = 0 ORDER BY create_time DESC")
    List<ExperimentProject> selectByApplicantId(@Param("applicantId") Long applicantId);

    /**
     * 更新项目风险信息
     * @param projectId 项目ID
     * @param riskScore 风险分数
     * @param riskLevel 风险等级
     * @return 影响行数
     */
    @Update("UPDATE t_experiment_project SET risk_score = #{riskScore}, risk_level = #{riskLevel} " +
            "WHERE id = #{projectId} AND deleted = 0")
    int updateRiskInfo(
            @Param("projectId") Long projectId,
            @Param("riskScore") Integer riskScore,
            @Param("riskLevel") String riskLevel
    );

    /**
     * 更新项目状态
     * @param projectId 项目ID
     * @param status 新状态
     * @return 影响行数
     */
    @Update("UPDATE t_experiment_project SET status = #{status} WHERE id = #{projectId} AND deleted = 0")
    int updateStatus(@Param("projectId") Long projectId, @Param("status") String status);

    /**
     * 批量更新项目状态
     * @param projectIds 项目ID列表
     * @param status 新状态
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("projectIds") List<Long> projectIds, @Param("status") String status);

    /**
     * 统计各风险等级项目数量
     * @return 统计结果
     */
    @Select("SELECT risk_level, COUNT(*) as count FROM t_experiment_project " +
            "WHERE deleted = 0 GROUP BY risk_level")
    List<Map<String, Object>> countByRiskLevel();

    /**
     * 统计各实验类型项目数量
     * @return 统计结果
     */
    @Select("SELECT experiment_type, COUNT(*) as count FROM t_experiment_project " +
            "WHERE deleted = 0 GROUP BY experiment_type")
    List<Map<String, Object>> countByExperimentType();

    /**
     * 统计各状态项目数量
     * @return 统计结果
     */
    @Select("SELECT status, COUNT(*) as count FROM t_experiment_project " +
            "WHERE deleted = 0 GROUP BY status")
    List<Map<String, Object>> countByStatus();

    /**
     * 查询高风险项目列表
     * @return 高风险项目列表
     */
    @Select("SELECT * FROM t_experiment_project WHERE risk_level IN ('高风险', '极高风险') " +
            "AND deleted = 0 ORDER BY risk_score DESC")
    List<ExperimentProject> selectHighRiskProjects();

    /**
     * 查询待评估项目列表
     * @return 待评估项目列表
     */
    @Select("SELECT * FROM t_experiment_project WHERE status = 'DRAFT' AND risk_score IS NULL " +
            "AND deleted = 0 ORDER BY create_time ASC")
    List<ExperimentProject> selectPendingAssessmentProjects();

    /**
     * 根据风险分数范围查询项目
     * @param minScore 最小分数
     * @param maxScore 最大分数
     * @return 项目列表
     */
    @Select("SELECT * FROM t_experiment_project WHERE risk_score >= #{minScore} AND risk_score <= #{maxScore} " +
            "AND deleted = 0 ORDER BY risk_score DESC")
    List<ExperimentProject> selectByRiskScoreRange(@Param("minScore") Integer minScore, @Param("maxScore") Integer maxScore);

    /**
     * 检查项目编号是否存在
     * @param projectCode 项目编号
     * @return 存在返回1,不存在返回0
     */
    @Select("SELECT COUNT(*) FROM t_experiment_project WHERE project_code = #{projectCode} AND deleted = 0")
    int checkProjectCodeExists(@Param("projectCode") String projectCode);
}