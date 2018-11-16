package sdk;

//IPAVH ����̨���Գ���
import java.util.Scanner;

public class IPAVHConsole {
	public static int SendFileGb(IPAVH handler) {
		// ��ȡ�������ļ���Դ
		FileResInfo file = new FileResInfo();
		if (handler.nGetFileRes(file, 1) == 0) {
			System.out.println(file.file_dir);
			System.out.println(file.file_name);

			FileResInfo[] files = new FileResInfo[1];
			files[0] = file;

			int[] tms = new int[1];
			tms[0] = 1;

			int id = handler.nCreateNewGbByFile(files, tms, false);
			System.out.println("�㲥��id = " + id);
			return id;
		}

		return 0;
	}

	// ��ӡ�ն���Ϣ
	public static void PrintTmInfo(TMInfo tm_info) {
		System.out.println("�ն�����:" + tm_info.name);
		System.out.println("�ն�IP:" + tm_info.ip);
		System.out.println("�ն˵�ǰ����״̬:" + tm_info.status);
		System.out.println("�ն˻���:" + tm_info.num);
		System.out.println("�ն�����:" + tm_info.vol);
		System.out.println("�Ƿ�����:" + tm_info.isOneLine);
		System.out.println("�ն���״̬:" + tm_info.isDoorSta);
		System.out.println("�Է��ն�ID:" + tm_info.otherTalkTmId);
		System.out.println("16������������״̬:" + tm_info.wKMBSta);
	}

	public static void main(String[] argv) {
		IPAVH handler = new IPAVH();
		int stream_id = 0;

		int version = handler.nGetSdkVer();
		System.out.println("ϵͳ�汾�� " + (version >>> 16) + "." + ((version << 16) >> 16));

		// ��¼�����·�������
		if (handler.nLogInByIP("admin", "admin", "172.29.96.10", 3960, true, false, 0) != 0) {
			System.out.println("��¼ʧ��......");
			return;
		}

		// ��ȡ�û���Ϣ
		UserInfo user_info = new UserInfo();
		int ret = handler.nGetUserConnMsg(user_info);
		System.out.println("�û�״ֵ̬:" + ret);
		System.out.println("�û�Ȩ��ֵ:" + user_info.userSc);
		System.out.println("�û����ն�:" + user_info.meBandTmid);
		System.out.println("�ն˸���:" + user_info.oneTm.length);
		System.out.println("");
		for (int i = 0; i < user_info.oneTm.length; i++) {
			PrintTmInfo(user_info.oneTm[i]);
		}
		System.out.println("");

		// ��ȡ�ն���Ϣ
		TMInfo tm_info = new TMInfo();
		int tmid = 1;
		ret = handler.nGetTmMsg(tm_info, tmid);
		if (ret == 0) {
			System.out.println("");
			System.out.println("�ն�id=" + tmid);
			PrintTmInfo(tm_info);
			System.out.println("");
		}

		System.out.println("�������ļ���Ϣ");
		// ��ȡ�ļ���Ϣ
		FileResInfo file;
		ret = 0;
		int first = 1;
		do {
			file = new FileResInfo();
			ret = handler.nGetFileRes(file, first);
			if (ret == 0) {
				System.out.println("  �ļ�Ŀ¼: " + file.file_dir);
				System.out.println("  �ļ�����: " + file.file_name);
			}

			first = 0;
		} while (ret == 0);
		System.out.println("");

		// ��ȡ������Ϣ
		System.out.println("������Ϣ��");
		String[] card_list = handler.nGetSysSoundCardInfo();
		for (int i = 0; i < card_list.length; i++) {
			System.out.println("  " + (i + 1) + " " + card_list[i]);
		}
		System.out.println("");

		// ����ϵͳ
		int is_win7 = handler.nIsSysGBWin7();
		if (is_win7 >= 1) {
			System.out.println("����ϵͳ: " + is_win7 + " win7 ������ϵͳ");
		} else {
			System.out.println("����ϵͳ: " + is_win7 + " ����win7ϵͳ");
		}

		// �ȴ��û���������
		System.out.println("");
		System.out.println("������");
		System.out.println("1 �����ļ��㲥");
		System.out.println("2 ɾ���㲥");
		System.out.println("# �˳�����");

		Scanner input = new Scanner(System.in);
		String val = null; // ��¼������ַ���
		do {
			val = input.next(); // �ȴ�����ֵ

			if (val.equals("1")) {
				stream_id = SendFileGb(handler);
			} else if (val.equals("2") && stream_id != 0) {
				handler.nDelGbStream(stream_id);
			}

		} while (!val.equals("#")); // ��������ֵ����#�ͼ�������

		System.out.println("��������\"#\"�������Ѿ��˳���");
		input.close();

		handler.cleanup();
	}
}
