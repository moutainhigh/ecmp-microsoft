package com.hq.ecmp.mscore.service.impl;
import java.util.*;

import com.google.common.collect.Maps;
import com.hq.api.system.domain.SysUser;
import com.hq.common.exception.BaseException;
import com.hq.common.utils.ServletUtils;
import com.hq.core.security.LoginUser;
import com.hq.core.security.service.TokenService;
import com.hq.ecmp.constant.*;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.dto.MessageDto;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.EcmpMessageService;
import com.hq.ecmp.mscore.service.IApproveTemplateNodeInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * (EcmpMessage)表服务实现类
 *
 * @author makejava
 * @since 2020-03-13 15:25:47
 */
@Service("ecmpMessageService")
public class EcmpMessageServiceImpl implements EcmpMessageService {
    @Resource
    private EcmpMessageMapper ecmpMessageDao;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private IApproveTemplateNodeInfoService approveTemplateNodeInfoService;
    @Autowired
    private DriverInfoMapper driverInfoMapper;
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private ApplyInfoMapper applyInfoMapper;
    @Autowired
    private RegimeInfoMapper regimeInfoMapper;
    @Autowired
    private CarGroupDispatcherInfoMapper carGroupDispatcherInfoMapper;
    @Autowired
    private CarGroupInfoMapper carGroupInfoMapper;
    @Autowired
    private ApplyApproveResultInfoMapper applyApproveResultInfoMapper;


    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public EcmpMessage queryById(Long id) {
        return this.ecmpMessageDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<EcmpMessage> queryAllByLimit(int offset, int limit) {
        return this.ecmpMessageDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param ecmpMessage 实例对象
     * @return 实例对象
     */
    @Override
    public EcmpMessage insert(EcmpMessage ecmpMessage) {
        this.ecmpMessageDao.insert(ecmpMessage);
        return ecmpMessage;
    }

    /**
     * 修改数据
     *
     * @param ecmpMessage 实例对象
     * @return 实例对象
     */
    @Override
    public EcmpMessage update(EcmpMessage ecmpMessage) {
        this.ecmpMessageDao.update(ecmpMessage);
        return this.queryById(ecmpMessage.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.ecmpMessageDao.deleteById(id) > 0;
    }

    /**
     * 插入一条消息
     * @param msgConstant
     * @param userConstant
     * @param ecmpId
     * @param url
     */
    @Override
    public void insertMessage(MsgConstant msgConstant, MsgTypeConstant type, MsgUserConstant userConstant, String content,Long ecmpId, String url) {
        EcmpMessage ecmpMessage = EcmpMessage.builder()
                .configType(userConstant.getType())
                .ecmpId(ecmpId)
                .status(MsgStatusConstant.MESSAGE_STATUS_T002.getType())
                .type(type.getType())
                .category(msgConstant.getDesp())
                .createTime(new Date())
                .content(content)
                .createBy(ecmpId)
                .url(url)
                .build();
        ecmpMessageDao.insert(ecmpMessage);
    }

    @Override
    public void insertMessage(MsgConstant msgConstant, MsgTypeConstant type, MsgUserConstant userConstant,String content, Long ecmpId) {
        insertMessage(msgConstant,type,userConstant,content,ecmpId,"");
    }

    @Override
    public List<EcmpMessage> selectMessageList(String identity) {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long ecmpId;
        List<Integer> configTypeList = new ArrayList();
        switch (identity){
            //乘客
            case "1":
                ecmpId = loginUser.getUser().getUserId();
                configTypeList.add(MsgUserConstant.MESSAGE_USER_APPROVAL.getType());
                configTypeList.add(MsgUserConstant.MESSAGE_USER_USER.getType());
                configTypeList.add(MsgUserConstant.MESSAGE_USER_DISPATCHER.getType());
                break;
            //司机
            case "2":
                ecmpId = loginUser.getDriver().getDriverId();
                configTypeList.add(MsgUserConstant.MESSAGE_USER_DRIVER.getType());
                break;
            default:
                throw new BaseException("错误信息");
        }
        Map map = new HashMap();
        map.put("configTypeList",configTypeList);
        map.put("ecmpId",ecmpId);
        return ecmpMessageDao.queryMessageList(map);
    }


    @Override
    public int getMessagesCount(String identity) {
        HttpServletRequest request = ServletUtils.getRequest();
        LoginUser loginUser = tokenService.getLoginUser(request);
        Long ecmpId;
        List<Integer> configTypeList = new ArrayList();
        switch (identity){
            //乘客
            case "1":
                ecmpId = loginUser.getUser().getUserId();
                configTypeList.add(MsgUserConstant.MESSAGE_USER_APPROVAL.getType());
                configTypeList.add(MsgUserConstant.MESSAGE_USER_USER.getType());
                configTypeList.add(MsgUserConstant.MESSAGE_USER_DISPATCHER.getType());
                break;
            //司机
            case "2":
                ecmpId = loginUser.getDriver().getDriverId();
                configTypeList.add(MsgUserConstant.MESSAGE_USER_DRIVER.getType());
                break;
            default:
                throw new BaseException("错误信息");
        }
        Map map = new HashMap();
        map.put("configTypeList",configTypeList);
        map.put("ecmpId",ecmpId);
        return ecmpMessageDao.queryMessageCount(map);
    }

    @Override
    public List<MessageDto> getMessagesForPassenger(SysUser user) throws Exception {
        String categorys="M001,M004,M006";//申请人
        if ("1".equals(user.getItIsDispatcher())){//调度员
            categorys+=",M003";
        }
        List<ApplyApproveResultInfo> approveTemplateNodeInfos = applyApproveResultInfoMapper.selectByUserId(null,user.getUserId(), ApproveStateEnum.WAIT_APPROVE_STATE.getKey());
        if (CollectionUtils.isNotEmpty(approveTemplateNodeInfos)){//审批员
            categorys+=",M002";
        }
        List<MessageDto> list = ecmpMessageDao.getMessagesForPassenger(user.getUserId(), categorys);
        if (CollectionUtils.isNotEmpty(list)){
            for (MessageDto messageDto:list){
                messageDto.setMessageTypeStr(MsgConstant.getDespByType(messageDto.getMessageType()));
                if (MsgConstant.MESSAGE_T006.getType().equals(messageDto.getMessageType())){
                    OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(messageDto.getMessageId());
                    messageDto.setUseCarMode(orderInfo.getUseCarMode());
                }
            }
        }
        return list;
    }

    @Override
    public List<MessageDto> getRunMessageForDrive(SysUser user) throws Exception {
        if ("0".equals(user.getItIsDriver())){
            throw new Exception("该用户不是司机");
        }
        DriverInfo driverInfo = driverInfoMapper.selectDriverInfoByUserId(user.getUserId());
        String categorys="M005,M004,M007";//申请人
        List<MessageDto> runMessageForDrive = ecmpMessageDao.getRunMessageForDrive(driverInfo.getDriverId(), categorys);
        //判断当前司机是不是调度员
        if ("1".equals(user.getItIsDispatcher())){
            List<MessageDto> runMessageForDispatcher = ecmpMessageDao.getRunMessageForDispatcher(user.getUserId(), "M003");
            runMessageForDrive.addAll(runMessageForDispatcher);
        }
        if (CollectionUtils.isNotEmpty(runMessageForDrive)){
            for (MessageDto messageDto:runMessageForDrive){
                messageDto.setMessageTypeStr(MsgConstant.getDespByType(messageDto.getMessageType()));
            }
        }
        //获取即将任务开始的通知
        OrderInfo info=orderInfoMapper.selectDriverOrder(driverInfo.getDriverId(), OrderState.ALREADYSENDING.getState());
        runMessageForDrive.add(new MessageDto(info.getOrderId(),MsgConstant.MESSAGE_T00.getType(),MsgConstant.MESSAGE_T00.getDesp(),1));
        return runMessageForDrive;
    }

    //TODO 审批通过后的发通知
    @Transactional
    public void saveApplyMessage(Long applyId,Long ecmpId,Long userId){
        Map<String,Object> map= Maps.newHashMap();
        map.put("messageId",applyId);
        map.put("ecmpId",ecmpId);
        map.put("category",MsgConstant.MESSAGE_T001);
        map.put("status",MsgStatusConstant.MESSAGE_STATUS_T002.getType());
        List<EcmpMessage> ecmpMessages = ecmpMessageDao.queryMessageList(map);
        if (CollectionUtils.isNotEmpty(ecmpMessages)){
            for (EcmpMessage message:ecmpMessages){
                message.setStatus(MsgStatusConstant.MESSAGE_STATUS_T001.getType());
                message.setUpdateBy(ecmpId);
                message.setUpdateTime(new Date());
                ecmpMessageDao.update(message);
            }
        }
        //通知调度员,通知申请人审批通过
        List<EcmpMessage> list=new ArrayList<>();
        //判断申请用车城市是否有我车队组织
        ApplyInfo applyInfo = applyInfoMapper.selectApplyInfoById(applyId);
        RegimeInfo regimeInfo = regimeInfoMapper.selectRegimeInfoById(applyInfo.getRegimenId());
//        if ("C001".equals(regimeInfo.getRuleCity())){
//
//            carGroupInfoMapper.selectCarGroupInfoById();
//        }else{
//
//        }
        EcmpMessage message=new EcmpMessage();
        message.setStatus(MsgStatusConstant.MESSAGE_STATUS_T002.getType());
        message.setCategory(MsgConstant.MESSAGE_T001.getType());
        message.setEcmpId(ecmpId);
        message.setConfigType(1);
    }
}