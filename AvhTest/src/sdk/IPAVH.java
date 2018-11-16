package sdk;
// java jni 类


// 回调函数抽象接口类
interface CBInterface {
	// 登录状态回调函数
	public abstract void connectOrDis(int is_con, UserInfo user_info);

	// 创建广播回调函数
	public abstract void newGbSta(int stream_id, int is_new, int is_new_ok);

	// 终端状态改变回调函数
	public abstract void terminalSta(TMInfo tm_info);

	// 终端触发32个扩展按键通知回调函数
	public abstract void terminalKeySta(int tmid, int key_val);

	// 消防状态回调函数
	public abstract void fireSta(int no, int val);

	// 终端报警回调函数
	public abstract void TmArmSta(int tmid, int arm_sta);

	// 绑定终端对讲状态回调函数
	public abstract void bandTmTalkSta(int call_sta, int call_tmid, int call_mbid);
}

// 具体实现回调类
class IPAVHCB implements CBInterface {
	public void connectOrDis(int is_con, UserInfo user_info) {
		parent.connectOrDis(is_con, user_info);
	}

	public void newGbSta(int stream_id, int is_new, int is_new_ok) {
		parent.newGbSta(stream_id, is_new, is_new_ok);
	}

	public void terminalSta(TMInfo tm_info) {
		parent.terminalSta(tm_info);
	}

	public void terminalKeySta(int tmid, int key_val) {
		parent.terminalKeySta(tmid, key_val);
	}

	public void fireSta(int no, int val) {
		parent.terminalKeySta(no, val);
	}

	public void TmArmSta(int tmid, int arm_sta) {
		parent.TmArmSta(tmid, arm_sta);
	}

	public void bandTmTalkSta(int call_sta, int call_tmid, int call_mbid) {
		parent.bandTmTalkSta(call_sta, call_tmid, call_mbid);
	}

	public void SetPaernt(IPAVH p) {
		parent = p;
	}

	public IPAVH GetParent() {
		return parent;
	}

	private IPAVH parent;
}

// IPAVH java类，负责调用C 动态库API
public class IPAVH {
	// 本地化API
	// SDK初始化
	public native boolean nInit();

	// SDK退出清理
	public native void nCleanup();

	// 设置回调函数
	public native int nSetUserCallBack(CBInterface cb);

	// 清除回调函数
	public native int nResetUserCallBack(CBInterface cb);

	// 登录服务器,通过IP地址登录
	// user_name 用户名
	// user_password 用户密码
	// ip 服务器IP地址
	// port 端口号，SDK从互联网登陆但服务器是内网IP，请服务器做端口映射(映射 2960,2962,3960端口)
	// auto_log 断线是否重连
	// synchro，是否异步登录，同步登陆成功或失败才返回,异步马上返回
	// band_tm_id 此用户同时绑定的终端ID，当为0时不綁定
	// 返回值: 登录状态,值为0表示登录成功
	public native int nLogInByIP(String user_name, String user_password, String ip, int port, boolean auto_log,
			boolean synchro, int band_tm_id);

	// 登录服务器,通过域名登录
	// user_name 用户名
	// user_password 用户密码
	// domain 域名地址
	// auto_log 断线是否重连
	// synchro，是否异步登录，同步登陆成功或失败才返回,异步马上返回
	// band_tm_id 此用户同时绑定的终端ID，当为0时不綁定
	// 返回值: 登录状态,值为0表示登录成功
	public native int nLogInByDomain(String user_name, String user_password, String domain, boolean auto_log,
			boolean synchro, int band_tm_id);

	// 断开与服务器的连接
	public native void nLogOut();

	// 获取连接状态
	// 返回值: 登录状态，如果登录成功，user_info存储用户信息
	public native int nGetUserConnMsg(UserInfo user_info);

	// 获取服务器文件资源
	// file 输出数据，文件目录,文件名称
	// from_first 1则从资源开头获取文件信息
	// 返回0表示获取成功
	// 循环调用此函数，可以获取到所有文件信息,返回1说明获取完毕
	public native int nGetFileRes(FileResInfo file, int from_first);

	// 获取一个终端信息
	// tm_info 终端信息
	// tmid 输入终端id
	// 返回值: 获取成功返回0，如果获取成功，tm_info存储终端信息
	public native int nGetTmMsg(TMInfo tm_info, int tmid);

	// 获取系统系统的声卡信息
	// 返回声卡名称数组
	public native String[] nGetSysSoundCardInfo();

	// 判断系统是否为WIN7及以上操作系统
	//
	public native int nIsSysGBWin7();

	// 创建文件广播
	// files 要发送的广播文件资源数组
	// tm 发送的终端id数组
	// loop 是否循环播放
	// 返回创建的广播流ID，失败返回0
	public native int nCreateNewGbByFile(FileResInfo files[], int tm[], boolean loop);

	// 创建声卡广播
	// soundcard_name 声卡名称
	// tm 发送的终端id数组
	// encls 可选择1(高音质)或2(低延时)类型
	// is_serSnd 是否强制服务器转发1:为是
	// 返回创建的广播流ID，失败返回0
	public native int nCreateNewGbBySoundCard(String sound_card_name, int tm[], int encls, int is_serSnd);

	// 创建绑定终端采播
	// tm 发送的终端id数组
	// encls 可选择1(高音质)或2(低延时)类型
	// is_serSnd 是否强制服务器转发1:为是
	// 返回创建的广播流ID，失败返回0
	public native int nCreateNewGbByTerminal(int tm[], int encls, int is_serSnd);

	// 创建TTS文本转语音广播
	// text 要发送的文本
	// type TTS解码声音类型 1:女声;2:男声;3:童声
	// play_count TTS解码播放次数
	// tm 发送的终端id数组
	// 返回创建的广播流ID，失败返回0
	public native int nCreateNewGbByTTS(String text, int type, int play_count, int tm[]);

	// 删除广播
	// stream_id 要删除广播流ID
	// 返回值：删除成功返回0
	public native int nDelGbStream(int stream_id);

	// 打开或关闭终端门
	// tmid 终端id
	// is_open 0，关闭，1 打开
	// 成功返回0
	public native int nOpenOrCloseTmDoor(int tmid, int is_open);

	// 控制终端报警IO输出
	// tmid 终端id
	// is_open 0，关闭，1 打开
	// 成功返回0
	public native int nOpenOrCloseTmJBOut(int tmid, int is_open);

	// 取消终端报警
	// tmid 终端id
	// 成功返回0
	public native int nCancelTmArm(int tmid);

	// 控制指定终端播放SD文件的播放状态
	// tmid 终端id
	// file 指定播放的文件名
	public native int nPlaySdFile(int tmid, FileResInfo file);

	// 获取服务器指定终端下载到SD卡里的文件名
	// file 输出数据，只获取文件名字段
	// from_first 1则从资源开头获取文件信息
	// 返回0表示获取成功
	// 循环调用此函数，可以获取到所有文件信息,返回1说明获取完毕
	public native int nGetSdFileName(FileResInfo file, int from_first);

	// 控制指定终端播放SD文件的播放状态
	// tmid 终端id
	// play_sta 0:停止播放，1，播放，2、暂停播放
	// 成功返回0
	public native int nCtrlSdFile(int tmid, int play_sta);

	// 控制绑定终端对讲
	// rqtype 对讲请求类型
	// 1 表示SDK绑定终端主动呼叫其它终端；
	// 2 表示SDK绑定终端主动断开当前对讲；
	// 3 表示SDK绑定终端主动接受其它终端呼叫；
	// call_tmid 对讲时对方终端ID号
	// call_tm_f
	// 1、SDK绑定终端主动呼叫其它终端时，呼叫其它终端的分机号(此终端连接多个呼叫面板，每个面板都有独立的地址码从1-15),可以为0。
	// 2、当Rqtype=3时CallTmF为0不接受呼叫，1接受
	// 成功返回0
	public native int nCtrlBandTmTalk(int rqtype, int call_tmid, int call_tm_f);

	// 获取SDK版本号
	// 2个高字节表示主版本，2个低字节表示次版本
	public native int nGetSdkVer();

	// 控制任意两个终端呼叫
	// main_tmid 主叫终端ID
	// other_tmid 被叫终端ID
	// other_tmf 被叫终端的分机号
	// 成功返回0
	public native int nCtrlAnyTmForCall(int main_tmid, int other_tmid, int other_tmf);

	// 控制当前终端断开呼叫对讲或接通呼叫对讲
	// any_tmid 要控制的终端ID
	// tsta 1为接通对讲，0为断开对讲或呼叫
	// 成功返回0
	public native int nCtrlAnyTmTalkSta(int any_tmid, int tsta);

	// 调节终端音量
	// any_tmid 要控制的终端ID
	// tvol 终端音量(1-100)
	// 成功返回0
	public native int nCtrlAnyTmVol(int any_tmid, int tvol);

	// 监听终端现场
	// main_tmid 监听操作终端ID
	// other_tmid 被监听终端ID
	// other_tmf 被监听终端的分机号
	// 成功返回0
	public native int nMonitorAnyTm(int main_tmid, int other_tmid, int other_tmf);

	// 退出监听终端现场
	// tmid 发起监听操作终端ID
	// 成功返回0
	public native int nMonitorExitAnyTm(int tmid);

	// 控制绑定终端对讲时将对方转接到其它终端
	// other_tmid 转接到其它终端ID
	// call_exten_type 转类型：
	// 0 不带任何参数转接
	// 1 带分机号参数转接
	// 2 带电话号码转接(此参数时otherTmid必须为电话终端)
	// other_tmf 转接终端的分机号
	// tel_no 转终端的电话号码(注意电话号码必须的ASC码)
	// 成功返回0
	public native int nCtrlBandTmCallForWard(int other_tmid, int call_exten_type, int other_tmf, byte[] tel_no);

	// 手动触发更新终端状态
	// 由于SDK并不是实时更新终端状态，所以如要更快的得到实时的终端状态可调用此接口,每调用一次更新当前终端的状态
	public native void nCtrlUpdateTmSta();

	// 设置系统声卡混音接口音量或选择此混音接口为系统默认接口
	// cap_mix_name 当前要设置的声卡混音接口名
	// set_type 设置此混音接口音量或者是为系统默认接口，SetType =1为设置音量，SetType =2为设置系统默认接口
	// mval 设置此混音接口音量值(0-100)
	public native void nSetSysSoundCardMix(String cap_mix_name, int set_type, int mval);

	// 构造函数
	public IPAVH() {
		bInit = false;
		bConnect = false;
		init();
	}

	// 初始化
	public void init() {
		if (!bInit) {
			bInit = true;
			bConnect = false;

			// 加载动态库
			System.loadLibrary("IPAVHWrapper");

			// 初始化sdk
			nInit();

			// 设置回调接口对象
			cb = new IPAVHCB();
			cb.SetPaernt(this);
			nSetUserCallBack(cb);
		}
	}

	// 清理
	public void cleanup() {
		if (bInit) {
			bInit = false;
			bConnect = false;

			nResetUserCallBack(cb);

			// SDK退出清理
			nCleanup();
		}
	}

	// 回调函数，派生类可以重写
	// 登陆状态回调函数
	// is_con sdk通知连接状态，此值为FALSE已断开
	public void connectOrDis(int is_con, UserInfo user_info) {
		if (is_con == 0) {
			bConnect = false;
			System.out.println("断开与服务器的连接......");
		} else {
			bConnect = true;
			System.out.println("登录成功......");
		}
	}

	// 创建广播回调函数
	public void newGbSta(int stream_id, int is_new, int is_new_ok) {
		if (is_new != 0) // 为新创建的广播，服务器回应是否创建成功
		{
			if (is_new_ok != 0) {
				System.out.println("广播创建成功......");
			} else {
				System.out.println("广播创建失败......");
			}
		} else // 此广播前面已创建成功，但此广播已停止
		{
			System.out.println("广播已停止......");
		}
	}

	// 终端状态回调函数
	public void terminalSta(TMInfo tm_info) {
		System.out.println("终端 " + tm_info.name + " 状态改变,新的状态值为: " + tm_info.status);
	}

	// 终端触发32个扩展按键通知回调函数
	public void terminalKeySta(int tmid, int key_val) {
		System.out.println("终端触发32个扩展按键通知回调");
	}

	// 消防状态回调函数
	// no 此消防信号所在32路消防采集终端机号(1-32) Fno=1 表示1-32信号,Fno=2 表示33-64信号,依次类推
	// val 32路信号状态(从字的低位为第一路信号),位为1表示有信号，位0无信号
	public void fireSta(int no, int val) {
		System.out.println("消防状态回调,终端号: " + no + "状态: " + val);
	}

	// 终端报警回调函数
	// tmid 报警终端ID
	// arm_sta 报警类型
	public void TmArmSta(int tmid, int arm_sta) {
		System.out.println("终端报警回调,终端号: " + tmid + "报警类型: " + arm_sta);
	}

	// 绑定终端对讲状态回调函数
	// call_sta 对讲状态
	// 1 表示有其它的终端呼叫SDK绑定终端
	// 2 表示SDK绑定终端与其它终端正在对讲
	// 3 表示SDK绑定终端呼叫其它终端，对方不接受呼叫
	// 4 表示SDK绑定终端呼叫其它终端失败
	// 6 表示有其它的终端呼叫SDK绑定终端已进入呼叫队列(注意此时并没有对绑定终端进入实际性呼叫流程，只是加入呼叫队列等待进入呼叫流程)
	// call_tmid 对讲时对方终端ID号
	// call_mbid 对讲时对方终端分机号
	public void bandTmTalkSta(int call_sta, int call_tmid, int call_mbid) {
		System.out.println("绑定终端对讲状态回调,对讲状态: " + call_sta + "对讲时对方终端ID号: " + call_tmid + "对讲时对方终端分机号: " + call_mbid);
	}

	// 私有成员变量
	private boolean bInit; // 是否初始化
	protected boolean bConnect; // 是否连接服务器
	private IPAVHCB cb;
}
