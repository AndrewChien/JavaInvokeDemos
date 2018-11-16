package sdk;

//IPAVH 控制台测试程序
import java.util.Scanner;

public class IPAVHConsole {
	public static int SendFileGb(IPAVH handler) {
		// 获取服务器文件资源
		FileResInfo file = new FileResInfo();
		if (handler.nGetFileRes(file, 1) == 0) {
			System.out.println(file.file_dir);
			System.out.println(file.file_name);

			FileResInfo[] files = new FileResInfo[1];
			files[0] = file;

			int[] tms = new int[1];
			tms[0] = 1;

			int id = handler.nCreateNewGbByFile(files, tms, false);
			System.out.println("广播流id = " + id);
			return id;
		}

		return 0;
	}

	// 打印终端信息
	public static void PrintTmInfo(TMInfo tm_info) {
		System.out.println("终端名称:" + tm_info.name);
		System.out.println("终端IP:" + tm_info.ip);
		System.out.println("终端当前工作状态:" + tm_info.status);
		System.out.println("终端机号:" + tm_info.num);
		System.out.println("终端音量:" + tm_info.vol);
		System.out.println("是否在线:" + tm_info.isOneLine);
		System.out.println("终端门状态:" + tm_info.isDoorSta);
		System.out.println("对方终端ID:" + tm_info.otherTalkTmId);
		System.out.println("16个外控面板连机状态:" + tm_info.wKMBSta);
	}

	public static void main(String[] argv) {
		IPAVH handler = new IPAVH();
		int stream_id = 0;

		int version = handler.nGetSdkVer();
		System.out.println("系统版本号 " + (version >>> 16) + "." + ((version << 16) >> 16));

		// 登录（建德服务器）
		if (handler.nLogInByIP("admin", "admin", "172.29.96.10", 3960, true, false, 0) != 0) {
			System.out.println("登录失败......");
			return;
		}

		// 获取用户信息
		UserInfo user_info = new UserInfo();
		int ret = handler.nGetUserConnMsg(user_info);
		System.out.println("用户状态值:" + ret);
		System.out.println("用户权限值:" + user_info.userSc);
		System.out.println("用户绑定终端:" + user_info.meBandTmid);
		System.out.println("终端个数:" + user_info.oneTm.length);
		System.out.println("");
		for (int i = 0; i < user_info.oneTm.length; i++) {
			PrintTmInfo(user_info.oneTm[i]);
		}
		System.out.println("");

		// 获取终端信息
		TMInfo tm_info = new TMInfo();
		int tmid = 1;
		ret = handler.nGetTmMsg(tm_info, tmid);
		if (ret == 0) {
			System.out.println("");
			System.out.println("终端id=" + tmid);
			PrintTmInfo(tm_info);
			System.out.println("");
		}

		System.out.println("服务器文件信息");
		// 获取文件信息
		FileResInfo file;
		ret = 0;
		int first = 1;
		do {
			file = new FileResInfo();
			ret = handler.nGetFileRes(file, first);
			if (ret == 0) {
				System.out.println("  文件目录: " + file.file_dir);
				System.out.println("  文件名称: " + file.file_name);
			}

			first = 0;
		} while (ret == 0);
		System.out.println("");

		// 获取声卡信息
		System.out.println("声卡信息：");
		String[] card_list = handler.nGetSysSoundCardInfo();
		for (int i = 0; i < card_list.length; i++) {
			System.out.println("  " + (i + 1) + " " + card_list[i]);
		}
		System.out.println("");

		// 操作系统
		int is_win7 = handler.nIsSysGBWin7();
		if (is_win7 >= 1) {
			System.out.println("操作系统: " + is_win7 + " win7 及以上系统");
		} else {
			System.out.println("操作系统: " + is_win7 + " 低于win7系统");
		}

		// 等待用户输入数据
		System.out.println("");
		System.out.println("请输入");
		System.out.println("1 发送文件广播");
		System.out.println("2 删除广播");
		System.out.println("# 退出程序");

		Scanner input = new Scanner(System.in);
		String val = null; // 记录输入的字符串
		do {
			val = input.next(); // 等待输入值

			if (val.equals("1")) {
				stream_id = SendFileGb(handler);
			} else if (val.equals("2") && stream_id != 0) {
				handler.nDelGbStream(stream_id);
			}

		} while (!val.equals("#")); // 如果输入的值不是#就继续输入

		System.out.println("你输入了\"#\"，程序已经退出！");
		input.close();

		handler.cleanup();
	}
}
