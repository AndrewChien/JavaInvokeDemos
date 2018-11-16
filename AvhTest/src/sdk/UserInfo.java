package sdk;

//用户信息
public class UserInfo {
	public int userSc; // 用户权限
	public int meBandTmid; // 用户綁定终端ID 不为0绑定成功

	public TMInfo[] oneTm; // 用户当前控制终端数组列表
}