package com.zhsj.api.dao;


import com.zhsj.api.bean.result.CountDealBean;
import com.zhsj.api.bean.result.CountDiscount;
import com.zhsj.api.bean.result.CountMember;
import com.zhsj.api.bean.result.RefundSta;
import com.zhsj.api.bean.result.OrderSta;
import com.zhsj.api.bean.result.StoreCountResult;
import com.zhsj.api.bean.result.StoreSta;
import com.zhsj.api.util.db.DynamicDataSource;
import com.zhsj.api.bean.OrderBean;
import com.zhsj.api.util.db.DS;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@DynamicDataSource(DS.DB_FLOW)
public interface TBOrderDao {

    int insertOrder(@Param("bean") OrderBean orderBean);

    int updateOrderByOrderId(@Param("status") int status,
                             @Param("orderId") String orderId);
    
    int updateByAccount(@Param("status") int status,
    					@Param("preStatus")int preStatus,
            			@Param("orderId") String orderId,
            			@Param("accountId")long accountId,
            			@Param("transactionId")String transactionId);
    
    int updateFYUP(@Param("status") int status,
						@Param("transactionId")String transactionId,
						@Param("cardType")String cardType,
						@Param("serviceCharge")double serviceCharge,
						@Param("rate")double rate,
						@Param("fuyouOrderNo")String fuyouOrderNo);

    int updateOrderByOrderIdIde(@Param("status") int status,
                             @Param("preStatus") int prestatus,
                             @Param("orderId") String orderId);

    OrderBean getByOrderId(@Param("orderId")String orderId);
    
    OrderBean getByTransactionId(@Param("transactionId")String transactionId);
    
    OrderBean getByTransactionIdAndStoreNo(@Param("transactionId")String transactionId,
    							 @Param("storeNo")String storeNo);

    OrderBean getById(@Param("id")long id);

    List<OrderBean> getMSAliListByCtime(@Param("id") long id,
                                        @Param("ctime") int ctime,
                                        @Param("pageSize") int pageSize);

    CountDealBean countDealBySaleId(@Param("saleId")long saleId,
                                    @Param("startTime")int startTime,
                                    @Param("endTime")int endTime);

    CountDealBean countDealByStoreNo(@Param("storeNo")String storeNo,
                                    @Param("startTime")int startTime,
                                    @Param("endTime")int endTime);

    CountDealBean countDealByParentNo(@Param("parentNO")String parentNO,
                                     @Param("startTime")int startTime,
                                     @Param("endTime")int endTime);


    CountDealBean countByParam(@Param("storeNo")String storeNo,
                               @Param("parentNo")String parentNo,
                               @Param("startTime")int startTime,
                               @Param("endTime")int endTime,
                               @Param("status") List status,
                               @Param("payMethod") int payMethod);

    List<OrderBean> getByParam(@Param("storeNo")String storeNo,
                               @Param("parentNo")String parentNo,
                               @Param("startTime")int startTime,
                               @Param("endTime")int endTime,
                               @Param("status") List status,
                               @Param("payMethod") int payMethod,
                               @Param("startNo") int startNo,
                               @Param("pageSize")int pageSize);

    CountDiscount countDiscount(@Param("discountId")long discountId);

    Integer countDiscountUser(@Param("discountId")long discountId);

    List<OrderBean> getDiscountOrder(@Param("discountId")long discountId,
                     @Param("startTime")int startTime,
                     @Param("endTime")int endTime,
                     @Param("startNo") int startNo,
                     @Param("pageSize")int pageSize);

    CountDiscount countDiscountOrder(@Param("discountId")long discountId,
                                     @Param("startTime")int startTime,
                                     @Param("endTime")int endTime);
    //xlc --20170306
	int getCountByMoney(@Param("storeNo")String storeNo,
			@Param("actualChargeAmount1")Double actualChargeAmount1,
			@Param("actualChargeAmount2")Double actualChargeAmount2);
	
	int getCountByTime(@Param("storeNo")String storeNo,@Param("time")int time);
	
	
	CountMember getByStoreNoAndUserId(@Param("storeNo")String storeNo,
			                              @Param("userId")long userId);
	
	double getSumMoneyByStoreNoAndUserId(@Param("storeNo")String storeNo, @Param("userId")long userId);
	
	List<CountMember> getByStoreNoAndMoney(@Param("storeNo")String storeNo,
			@Param("actualChargeAmount1")Double actualChargeAmount1,
			@Param("actualChargeAmount2")Double actualChargeAmount2);
	
	List<CountMember> getByStoreNoAndTime(@Param("storeNo")String storeNo,@Param("time")int time);
	
	Double getOrgDiscountPrice(@Param("storeNo")String storeNo,
							   @Param("startTime") int startTime,
							   @Param("endTime")int endTime);
	
	Integer countOrgDiscountPrice(@Param("storeNo")String storeNo,
								  @Param("startTime") int startTime,
								  @Param("endTime")int endTime);
	
	Map<String, Object> countByUserAndTime(@Param("storeNo")String storeNo,
										  @Param("startTime") int startTime,
										  @Param("endTime")int endTime,
										  @Param("accountId")long accountId,
										  @Param("statuses")List<Integer> statuses);
	Map<String, Object> countRefundByUserAndTime(@Param("storeNo")String storeNo,
												  @Param("startTime") int startTime,
												  @Param("endTime")int endTime,
												  @Param("accountId")long accountId);
	
	Map<String, Object> countStoreDisByUserAndTime(@Param("storeNo")String storeNo,
													  @Param("startTime") int startTime,
													  @Param("endTime")int endTime,
													  @Param("accountId")long accountId,
													  @Param("statuses")List<Integer> statuses);
	Map<String, Object> countOrgDisByUserAndTime(@Param("storeNo")String storeNo,
												  @Param("startTime") int startTime,
												  @Param("endTime")int endTime,
												  @Param("accountId")long accountId,
												  @Param("statuses")List<Integer> statuses);
	
	Map<String, Object> countByUserTimeMethod(@Param("storeNo")String storeNo,
			  @Param("startTime") int startTime,
			  @Param("endTime")int endTime,
			  @Param("accountId")long accountId,
			  @Param("statuses")List<Integer> statuses,
			  @Param("payMethod")String payMethod);
	
	
	List<OrderBean> getByStatusAndCtime(@Param("status")int status,
										@Param("startTime")int startTime,
										@Param("endTime")int endTime);
	
	StoreCountResult countStore(@Param("storeNo")String storeNo,
								@Param("parentStoreNo")String parentStoreNo,
								  @Param("startTime") int startTime,
								  @Param("endTime")int endTime);
	
	int countStoreRefund(@Param("storeNo")String storeNo,
						@Param("parentStoreNo")String parentStoreNo,
						 @Param("startTime") int startTime,
						  @Param("endTime")int endTime);
	
	
    List<OrderBean> getListByParamMap(Map<String, Object> paramMap);
    
    int getCountByParamMap(Map<String, Object> paramMap);
    
    OrderSta getOrderStaByParamMap(Map<String, Object> paramMap);
    
    RefundSta getRefundStaByParamMap(Map<String, Object> paramMap);
	
	OrderBean getByOrderIdOrTransId(@Param("storeNos")List<String> storeNos, @Param("orderId")String orderId, @Param("transId")String transId);
	
	OrderBean getByTypeOrderIdOrTransId(@Param("storeNo")String storeNo,
										@Param("orderId")String orderId, 
										@Param("transId")String transId,
										@Param("payType")int payType);
	
	int updateOrderRefundById(@Param("id")long id,@Param("refundNo")String refundNo,@Param("refundMoney")double refundMoney, @Param("status")int status);
	
	int updateStatusById(@Param("id")long id, @Param("status")int status);

	OrderSta getTodayOrderSta(@Param("storeNo")String storeNo, @Param("startTime")int startTime, @Param("endTime")int endTime);

	RefundSta getTodayRefundSta(@Param("storeNo")String storeNo, @Param("startTime")int startTime, @Param("endTime")int endTime);
	
    StoreSta getByTodayStoreSta(@Param("storeNo")String storeNo, @Param("startTime")int startTime, @Param("endTime")int endTime);
    
    double getTodayRefundMoney(@Param("storeNo")String storeNo, @Param("startTime")int startTime, @Param("endTime")int endTime);
    
	int updateStatusAndMoney(@Param("id")long id, 
							 @Param("status")int status,
							 @Param("refundMoney")double refundMoney,
							 @Param("refundNo") String refundNo,
							 @Param("serviceCharge")double service_charge);
	
	int updateStatus(@Param("id")long id, 
			 		@Param("status")int status,
			 		@Param("preStatus")int preStatus,
			 		@Param("transactionId")String transactionId,
			 		@Param("userId")long userId);
	
	int updateStatusByTransacionId(@Param("status")int status,
							 		@Param("preStatus")int preStatus,
							 		@Param("transactionId")String transactionId,
							 		@Param("totalFee")double totalFee);
	
	int updateUser(@Param("id")long id, 
			 		@Param("transactionId")String transactionId,
			 		@Param("userId")long userId);
	
	Map<String, Object> getAmSum(@Param("storeNos")List<String> storeNos,
			@Param("startTime")int startTime,@Param("endTime")int endTime,
			@Param("payType")int payType,@Param("payMethod")String payMethod,
			@Param("statusList")List<Integer> statusList);
	
	OrderBean getBy3Id(@Param("mchnt_order_no")String mchnt_order_no,
					   @Param("transaction_id")String transaction_id,
					   @Param("wwt_order_no")String wwt_order_no,
					   @Param("storeNo")String storeNo);
	
	
	List<OrderBean> getByUser(@Param("userId") long userId,
							  @Param("storeNo")String storeNo,
							  @Param("startTime")long startTime,
							  @Param("endTime")long endTime,
							  @Param("statusList")List statusList);
	
//	Map<String, Object> getRefundSum(@Param("storeNos")List<String> storeNos, 
//			@Param("startTime")int startTime, @Param("endTime")int endTime, @Param("payType")int payType, @Param("payMethod")String payMethod);
}
