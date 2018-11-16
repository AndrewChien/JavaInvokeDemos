package sdk;

//终端信息
public class TMInfo {
	public String name; // 终端名
	public String ip; // 终端IP
	public int status; // 终端当前工作状态: 0 空闲 1 点播文件 2 接收广播 3 终端本身发起采播或文件广播 4 正在对讲 5 主叫状态 6 被叫状态
	public int num; // 终端机号
	public int vol; // 终端音量
	public int isOneLine; // 是否在线，1:在线;0:离线
	public int isDoorSta; // 终端门状态 1开，0为关
	public int otherTalkTmId; // 如终端在对讲，此处为对方终端ID
	public int wKMBSta; // 16个外控面板连机状态,字的位0表示面板地址为0的状态,1在线,0不在线
}