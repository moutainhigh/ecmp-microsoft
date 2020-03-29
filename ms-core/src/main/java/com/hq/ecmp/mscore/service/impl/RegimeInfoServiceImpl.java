package com.hq.ecmp.mscore.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.hq.ecmp.constant.OrderConstant;
import com.hq.ecmp.mscore.domain.*;
import com.hq.ecmp.mscore.mapper.*;
import com.hq.ecmp.mscore.service.*;
import com.hq.ecmp.mscore.vo.*;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.util.StringUtil;
import com.hq.common.utils.DateUtils;
import com.hq.ecmp.constant.CarConstant;
import com.hq.ecmp.mscore.vo.RegimenVO;

import javax.annotation.Resource;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hqer
 * @date 2020-01-02
 */
@Service
public class RegimeInfoServiceImpl implements IRegimeInfoService {

    @Autowired
    private UserRegimeRelationInfoMapper userRegimeRelationInfoMapper;
    @Autowired
    private RegimeInfoMapper regimeInfoMapper;
    @Autowired
    private SceneRegimeRelationMapper sceneRegimeRelationMapper;
    @Autowired
    private IRegimeUseCarCityRuleInfoService regimeUseCarCityRuleInfoService;
    @Autowired
    private IRegimeUseCarTimeRuleInfoService regimeUseCarTimeRuleInfoService;
    @Autowired
    private ISceneInfoService sceneInfoService;
    @Autowired
    private CarGroupServeScopeInfoMapper carGroupServeScopeInfoMapper;
    @Autowired
	private ApproveTemplateNodeInfoMapper approveTemplateNodeInfoMapper;
    @Resource
	private ThirdService thirdService;
    @Resource
	private OrderAddressInfoMapper orderAddressInfoMapper;
    @Resource
	private OrderInfoMapper orderInfoMapper;


    /**
     * 根据用车制度id查询用车值得详细信息
     *
     * @param regimenId 【请填写功能名称】ID
     * @return 【请填写功能名称】
     */
    @Override
    public RegimeInfo selectRegimeInfoById(Long regimenId) {
        return regimeInfoMapper.selectRegimeInfoById(regimenId);
    }

    /**
     * 查询所有用车制度信息
     * @return
     */
    @Override
    public List<RegimeInfo> selectAll() {
        return regimeInfoMapper.selectAll();
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param regimeInfo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<RegimeInfo> selectRegimeInfoList(RegimeInfo regimeInfo) {
        return regimeInfoMapper.selectRegimeInfoList(regimeInfo);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param regimeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertRegimeInfo(RegimeInfo regimeInfo) {
        regimeInfo.setCreateTime(DateUtils.getNowDate());
        return regimeInfoMapper.insertRegimeInfo(regimeInfo);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param regimeInfo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateRegimeInfo(RegimeInfo regimeInfo) {
        regimeInfo.setUpdateTime(DateUtils.getNowDate());
        return regimeInfoMapper.updateRegimeInfo(regimeInfo);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param regimenIds 需要删除的【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteRegimeInfoByIds(Long[] regimenIds) {
        return regimeInfoMapper.deleteRegimeInfoByIds(regimenIds);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param regimenId 【请填写功能名称】ID
     * @return 结果
     */
    @Override
    public int deleteRegimeInfoById(Long regimenId) {
        return regimeInfoMapper.deleteRegimeInfoById(regimenId);
    }

    /**
     * 根据用户id查询用车制度集合(有场景id,则加场景条件)
     *
     * @param userId
     * @return
     */
    @Override
    public List<RegimenVO> findRegimeInfoListByUserId(Long userId, Long sceneId) {
        //根据userId查询regimeId集合
        List<Long> regimeIds = userRegimeRelationInfoMapper.selectIdsByUserId(userId);
        //如果有制度条件限制,则进行条件筛选
        if(sceneId != null){
            //根据sceneId查询制度id集合
            List<Long> regimenIds2 = sceneRegimeRelationMapper.selectRegimenIdsBySceneId(sceneId);
            //求交集
            regimeIds.retainAll(regimenIds2);
        }
        //根据regimeId集合查询RegimeInfo集合
		List<RegimenVO> regimeInfoList = new ArrayList<>();
		for (Long regimeId : regimeIds) {
			RegimenVO regimenVO = regimeInfoMapper.selectRegimenVOById(regimeId);
			//查询制度对应的审批第一个节点类型
			ApproveTemplateNodeInfo approveTemplateNodeInfo = approveTemplateNodeInfoMapper.selectFirstOpproveNode(regimeId);
			if(ObjectUtils.isNotEmpty(approveTemplateNodeInfo)){
				String approverType = approveTemplateNodeInfo.getApproverType();
				regimenVO.setFirstOpproveNodeTypeIsProjectLeader(approverType == "T004" ? true : false);
			}
			regimeInfoList.add(regimenVO);
		}
        return regimeInfoList;
    }

	@Override
	public boolean findOwnCar(Long regimenId) {
		RegimeInfo regimeInfo = selectRegimeInfoById(regimenId);
		String canUseCarMode = regimeInfo.getCanUseCarMode();
		if(StringUtil.isEmpty(canUseCarMode)){
			return false;
		}
		List<String> list = Arrays.asList(canUseCarMode.split(","));
		if(list.contains(CarConstant.USR_CARD_MODE_HAVE)){
			return true;
		}else{
			return false;
		}
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean createRegime(RegimePo regimePo) {
		//生成制度
		Integer insertRegimeFlag = regimeInfoMapper.insertRegime(regimePo);
		Long regimenId=regimePo.getRegimenId();
		//生成城市限制记录
		List<String> cityLimitIds = regimePo.getCityLimitIds();
		if(null !=cityLimitIds && cityLimitIds.size()>0){
			List<RegimeUseCarCityRuleInfo> regimeUseCarCityRuleInfoList=new ArrayList<RegimeUseCarCityRuleInfo>(cityLimitIds.size());
			String ruleCity = regimePo.getRuleCity();
			String ruleAction=null;
			if(null !=ruleCity && "C002".equals(ruleCity) ){
				ruleAction="Y000";//在地点内的城市
			}else if(null !=ruleCity && "C003".equals(ruleCity)){
				ruleAction="N001";//在地点外的城市
			}
			for (String cityCode : cityLimitIds) {
				regimeUseCarCityRuleInfoList.add(new RegimeUseCarCityRuleInfo(regimenId, ruleAction, cityCode, regimePo.getOptId(), new Date()));
			}
			regimeUseCarCityRuleInfoService.batchInsert(regimeUseCarCityRuleInfoList);
		}
	
		//公务生成用车时间段限制记录
		List<RegimeUseCarTimeRuleInfo> regimeUseCarTimeRuleInfoList = regimePo.getRegimeUseCarTimeRuleInfoList();
		if(null !=regimeUseCarTimeRuleInfoList && regimeUseCarTimeRuleInfoList.size()>0){
			for (RegimeUseCarTimeRuleInfo regimeUseCarTimeRuleInfo : regimeUseCarTimeRuleInfoList) {
				regimeUseCarTimeRuleInfo.setRuleAction("Y000");//在限制时间段内
				regimeUseCarTimeRuleInfo.setRegimenId(regimenId);
				regimeUseCarTimeRuleInfo.setCreateBy(regimePo.getOptId());
				regimeUseCarTimeRuleInfo.setCreateTime(new Date());
			}
			regimeUseCarTimeRuleInfoService.batchInsert(regimeUseCarTimeRuleInfoList);
		}
		
		List<Long> userList = regimePo.getUserList();
		if(null !=userList && userList.size()>0){
			//生成可用用户-制度中间记录
			userRegimeRelationInfoMapper.batchInsertUser(regimenId, userList);
		}
		Long sceneId = regimePo.getSceneId();
		if(null !=sceneId){
			//生成制度-场景记录
			SceneRegimeRelation sceneRegimeRelation = new SceneRegimeRelation();
			sceneRegimeRelation.setSceneId(sceneId);
			sceneRegimeRelation.setRegimenId(regimenId);
			sceneRegimeRelationMapper.insertSceneRegimeRelation(sceneRegimeRelation);
			
		}
		return true;
	}

	@Override
	public List<RegimeVo> queryRegimeList(RegimeQueryPo regimeQueryPo) {
		List<RegimeVo> regimeList = regimeInfoMapper.queryRegimeList(regimeQueryPo);
		if(null !=regimeList && regimeList.size()>0){
			for (RegimeVo regimeVo : regimeList) {
				//查询该制度的使用人数
				Integer userCount = userRegimeRelationInfoMapper.queryRegimeUserCount(regimeVo.getRegimeId());
				regimeVo.setUseNum(userCount);
			}
		}
		return regimeList;
	}

	@Override
	public Integer queryRegimeListCount(RegimeQueryPo regimeQueryPo) {
		return regimeInfoMapper.queryRegimeListCount(regimeQueryPo);
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	@Override
	public boolean optRegime(RegimeOpt regimeOpt) {
		String optType = regimeOpt.getOptType();
		if("Y000".equals(optType) || "N111".equals(optType)){
			regimeInfoMapper.updateStatus(regimeOpt);
		}else{
			//物理删除
			regimeInfoMapper.deleteRegimeInfoById(regimeOpt.getRegimeId());
			userRegimeRelationInfoMapper.deleteUserRegimeRelationInfoByRegimeId(regimeOpt.getRegimeId());
			sceneRegimeRelationMapper.deleteSceneRegimeRelationByRegimeId(regimeOpt.getRegimeId());
		}
		return true;
	}

	@Override
	public RegimeInfo queryRegimeType(Long regimeId) {
		return regimeInfoMapper.queryRegimeType(regimeId);
	}

	@Override
	public RegimeVo queryRegimeDetail(Long regimeId) {
		RegimeVo regimeVo = regimeInfoMapper.queryRegimeDetail(regimeId);
		if(null !=regimeVo){
			//查询对应的场景名称
			SceneInfo sceneInfo = sceneInfoService.querySceneByRegimeId(regimeId);
			if(null !=sceneInfo){
				regimeVo.setSceneName(sceneInfo.getName());
			}
			//查询制度使用人数
			Integer userCount = userRegimeRelationInfoMapper.queryRegimeUserCount(regimeId);
			regimeVo.setUseNum(userCount);
		}
		return regimeVo;
	}

	@Override
	public RegimeInfo queryUseCarModelByNoteId(Long noteId) {
		return regimeInfoMapper.queryUseCarModelByNoteId(noteId);
	}

	@Override
	public String queryUseCarModelByJourneyId(Long journeyId) {
		
		return null;
	}

	/**
	 * 获取用户可用网约车型
	 * @return
	 */
	@Override
	public String getUserOnlineCarLevels(Long regimenId) {
		return regimeInfoMapper.getUserOnlineCarLevels(regimenId);
	}

	@Override
	public boolean judgeNotDispatch(Long regimeId, String cityCode) {
		if(StringUtil.isEmpty(cityCode)){
			return false;
		}
		RegimeInfo regimeInfo = regimeInfoMapper.selectRegimeInfoById(regimeId);
		if(null==regimeInfo){
			return false;
		}
		String canUseCarMode = regimeInfo.getCanUseCarMode();
		if(StringUtil.isEmpty(canUseCarMode)){
			return false;
		}
		String[] split = canUseCarMode.split(",");
		List<String> asList = Arrays.asList(split);
		if(asList.size()==1){
			if(CarConstant.USR_CARD_MODE_HAVE.equals(asList.get(0))){
				return false;
			}else{
				return true;
			}
		}
		//自有车+网约车时，但上车地点“不在”车队的用车城市 范围内
		List<Long> result = carGroupServeScopeInfoMapper.queryCarGroupByCity(cityCode);
		if(null !=result && result.size()>0){
			return false;
		}
		return true;
	}

	/**
	 * app端查询用车制度详情
	 * @param regimenId
	 * @return
	 */
	@Override
	public RegimeVo selectRegimeDetailById(Long regimenId) {
		RegimeVo regimeVo = regimeInfoMapper.queryRegimeDetail(regimenId);
		if(ObjectUtils.isEmpty(regimenId)){
			throw new RuntimeException("查询制度详情失败");
		}
		return regimeVo;
	}

	@Override
	public String queryCarModeLevel(Long orderId, String useCarMode) {
		String carModeLevel;
		RegimeVo regimeVo = regimeInfoMapper.queryRegimeInfoByOrderId(orderId);
		String regimenType = regimeVo.getRegimenType();
		if (StringUtil.isNotEmpty(useCarMode)) {
			// 传入了用车方式
			if (CarConstant.USR_CARD_MODE_NET.equals(useCarMode)) {
				// 用车方式-网约车
				if (CarConstant.USE_CAR_TYPE_OFFICIAL.equals(regimenType)) {
					// 公务
					carModeLevel = regimeVo.getUseCarModeOnlineLevel();
				} else {
					// 差旅
					carModeLevel = regimeVo.getTravelUseCarModeOnlineLevel();
				}
			} else {
				// 用车方式-自有车
				if (CarConstant.USE_CAR_TYPE_OFFICIAL.equals(regimenType)) {
					carModeLevel = regimeVo.getUseCarModeOwnerLevel();
				} else {
					carModeLevel = regimeVo.getTravelUseCarModeOwnerLevel();
				}
			}
		} else {
			// 没有传用车方式 则默认取网约车的车型配置
			if (CarConstant.USE_CAR_TYPE_OFFICIAL.equals(regimenType)) {
				carModeLevel = regimeVo.getUseCarModeOnlineLevel();
			} else {
				carModeLevel = regimeVo.getTravelUseCarModeOnlineLevel();
			}
		}
		return carModeLevel;
	}

	/**
	 * 通过订单，用车方式获取车型以及预估价格等相关信息
	 * @param orderId
	 * @param useCarMode,不传默认为网约车 W100-自有车 	W200-网约车
	 */
	public List<CarLevelAndPriceReVo> getCarlevelAndPriceByOrderId(Long orderId,String useCarMode){
		Date bookingStartTime = null;
		String groupIds = queryCarModeLevel(orderId, useCarMode);
		OrderInfo orderInfo = orderInfoMapper.selectOrderInfoById(orderId);
		EstimatePriceVo estimatePriceVo = new EstimatePriceVo();
		estimatePriceVo.setServiceType(Integer.parseInt(orderInfo.getServiceType()));
		estimatePriceVo.setGroups(groupIds);
		OrderAddressInfo orderAddressInfo = new OrderAddressInfo();
		orderAddressInfo.setOrderId(orderId);
		List<OrderAddressInfo> orderAddressInfos = orderAddressInfoMapper.selectOrderAddressInfoList(orderAddressInfo);
		for (OrderAddressInfo orderAddressInfoCh:
			 orderAddressInfos) {
			if(orderAddressInfoCh.getType().equals(OrderConstant.ORDER_ADDRESS_ACTUAL_SETOUT)){
				estimatePriceVo.setCityId(orderAddressInfoCh.getCityPostalCode());
				estimatePriceVo.setBookingDate((orderAddressInfoCh.getActionTime().getTime()+"").substring(0,10));
				bookingStartTime = orderAddressInfoCh.getActionTime();
				estimatePriceVo.setBookingStartAddr(orderAddressInfoCh.getAddress());
				estimatePriceVo.setBookingStartPointLo(orderAddressInfoCh.getLongitude()+"");
				estimatePriceVo.setBookingStartPointLa(orderAddressInfoCh.getLatitude()+"");
			}else if(orderAddressInfoCh.getType().equals(OrderConstant.ORDER_ADDRESS_ACTUAL_ARRIVE)){
				estimatePriceVo.setBookingEndAddr(orderAddressInfoCh.getAddress());
				estimatePriceVo.setBookingEndPointLo(orderAddressInfoCh.getLongitude()+"");
				estimatePriceVo.setBookingEndPointLa(orderAddressInfoCh.getLatitude()+"");
			}
		}
		List<CarCostVO> carCostVOS = thirdService.enterpriseOrderGetCalculatePrice(estimatePriceVo);
		List<CarLevelAndPriceReVo> result = new ArrayList<>();
		String[] split = groupIds.split(",|，");
		for (int i = 0; i < carCostVOS.size() ; i++) {
			CarCostVO carCostVO = carCostVOS.get(i);
			CarLevelAndPriceReVo carLevelAndPriceReVo = new CarLevelAndPriceReVo();
			carLevelAndPriceReVo.setDuration(Integer.parseInt(carCostVO.getDuration()));
			carLevelAndPriceReVo.setOnlineCarLevel(split[i]);
			carLevelAndPriceReVo.setEstimatePrice(carCostVO.getDisMoney());
			carLevelAndPriceReVo.setSource(carCostVO.getSource());
			carLevelAndPriceReVo.setBookingStartTime(bookingStartTime);
			result.add(carLevelAndPriceReVo);
		}
		return result;
	}
}
