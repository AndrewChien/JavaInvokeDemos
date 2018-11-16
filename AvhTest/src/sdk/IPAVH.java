package sdk;
// java jni ��


// �ص���������ӿ���
interface CBInterface {
	// ��¼״̬�ص�����
	public abstract void connectOrDis(int is_con, UserInfo user_info);

	// �����㲥�ص�����
	public abstract void newGbSta(int stream_id, int is_new, int is_new_ok);

	// �ն�״̬�ı�ص�����
	public abstract void terminalSta(TMInfo tm_info);

	// �ն˴���32����չ����֪ͨ�ص�����
	public abstract void terminalKeySta(int tmid, int key_val);

	// ����״̬�ص�����
	public abstract void fireSta(int no, int val);

	// �ն˱����ص�����
	public abstract void TmArmSta(int tmid, int arm_sta);

	// ���ն˶Խ�״̬�ص�����
	public abstract void bandTmTalkSta(int call_sta, int call_tmid, int call_mbid);
}

// ����ʵ�ֻص���
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

// IPAVH java�࣬�������C ��̬��API
public class IPAVH {
	// ���ػ�API
	// SDK��ʼ��
	public native boolean nInit();

	// SDK�˳�����
	public native void nCleanup();

	// ���ûص�����
	public native int nSetUserCallBack(CBInterface cb);

	// ����ص�����
	public native int nResetUserCallBack(CBInterface cb);

	// ��¼������,ͨ��IP��ַ��¼
	// user_name �û���
	// user_password �û�����
	// ip ������IP��ַ
	// port �˿ںţ�SDK�ӻ�������½��������������IP������������˿�ӳ��(ӳ�� 2960,2962,3960�˿�)
	// auto_log �����Ƿ�����
	// synchro���Ƿ��첽��¼��ͬ����½�ɹ���ʧ�ܲŷ���,�첽���Ϸ���
	// band_tm_id ���û�ͬʱ�󶨵��ն�ID����Ϊ0ʱ������
	// ����ֵ: ��¼״̬,ֵΪ0��ʾ��¼�ɹ�
	public native int nLogInByIP(String user_name, String user_password, String ip, int port, boolean auto_log,
			boolean synchro, int band_tm_id);

	// ��¼������,ͨ��������¼
	// user_name �û���
	// user_password �û�����
	// domain ������ַ
	// auto_log �����Ƿ�����
	// synchro���Ƿ��첽��¼��ͬ����½�ɹ���ʧ�ܲŷ���,�첽���Ϸ���
	// band_tm_id ���û�ͬʱ�󶨵��ն�ID����Ϊ0ʱ������
	// ����ֵ: ��¼״̬,ֵΪ0��ʾ��¼�ɹ�
	public native int nLogInByDomain(String user_name, String user_password, String domain, boolean auto_log,
			boolean synchro, int band_tm_id);

	// �Ͽ��������������
	public native void nLogOut();

	// ��ȡ����״̬
	// ����ֵ: ��¼״̬�������¼�ɹ���user_info�洢�û���Ϣ
	public native int nGetUserConnMsg(UserInfo user_info);

	// ��ȡ�������ļ���Դ
	// file ������ݣ��ļ�Ŀ¼,�ļ�����
	// from_first 1�����Դ��ͷ��ȡ�ļ���Ϣ
	// ����0��ʾ��ȡ�ɹ�
	// ѭ�����ô˺��������Ի�ȡ�������ļ���Ϣ,����1˵����ȡ���
	public native int nGetFileRes(FileResInfo file, int from_first);

	// ��ȡһ���ն���Ϣ
	// tm_info �ն���Ϣ
	// tmid �����ն�id
	// ����ֵ: ��ȡ�ɹ�����0�������ȡ�ɹ���tm_info�洢�ն���Ϣ
	public native int nGetTmMsg(TMInfo tm_info, int tmid);

	// ��ȡϵͳϵͳ��������Ϣ
	// ����������������
	public native String[] nGetSysSoundCardInfo();

	// �ж�ϵͳ�Ƿ�ΪWIN7�����ϲ���ϵͳ
	//
	public native int nIsSysGBWin7();

	// �����ļ��㲥
	// files Ҫ���͵Ĺ㲥�ļ���Դ����
	// tm ���͵��ն�id����
	// loop �Ƿ�ѭ������
	// ���ش����Ĺ㲥��ID��ʧ�ܷ���0
	public native int nCreateNewGbByFile(FileResInfo files[], int tm[], boolean loop);

	// ���������㲥
	// soundcard_name ��������
	// tm ���͵��ն�id����
	// encls ��ѡ��1(������)��2(����ʱ)����
	// is_serSnd �Ƿ�ǿ�Ʒ�����ת��1:Ϊ��
	// ���ش����Ĺ㲥��ID��ʧ�ܷ���0
	public native int nCreateNewGbBySoundCard(String sound_card_name, int tm[], int encls, int is_serSnd);

	// �������ն˲ɲ�
	// tm ���͵��ն�id����
	// encls ��ѡ��1(������)��2(����ʱ)����
	// is_serSnd �Ƿ�ǿ�Ʒ�����ת��1:Ϊ��
	// ���ش����Ĺ㲥��ID��ʧ�ܷ���0
	public native int nCreateNewGbByTerminal(int tm[], int encls, int is_serSnd);

	// ����TTS�ı�ת�����㲥
	// text Ҫ���͵��ı�
	// type TTS������������ 1:Ů��;2:����;3:ͯ��
	// play_count TTS���벥�Ŵ���
	// tm ���͵��ն�id����
	// ���ش����Ĺ㲥��ID��ʧ�ܷ���0
	public native int nCreateNewGbByTTS(String text, int type, int play_count, int tm[]);

	// ɾ���㲥
	// stream_id Ҫɾ���㲥��ID
	// ����ֵ��ɾ���ɹ�����0
	public native int nDelGbStream(int stream_id);

	// �򿪻�ر��ն���
	// tmid �ն�id
	// is_open 0���رգ�1 ��
	// �ɹ�����0
	public native int nOpenOrCloseTmDoor(int tmid, int is_open);

	// �����ն˱���IO���
	// tmid �ն�id
	// is_open 0���رգ�1 ��
	// �ɹ�����0
	public native int nOpenOrCloseTmJBOut(int tmid, int is_open);

	// ȡ���ն˱���
	// tmid �ն�id
	// �ɹ�����0
	public native int nCancelTmArm(int tmid);

	// ����ָ���ն˲���SD�ļ��Ĳ���״̬
	// tmid �ն�id
	// file ָ�����ŵ��ļ���
	public native int nPlaySdFile(int tmid, FileResInfo file);

	// ��ȡ������ָ���ն����ص�SD������ļ���
	// file ������ݣ�ֻ��ȡ�ļ����ֶ�
	// from_first 1�����Դ��ͷ��ȡ�ļ���Ϣ
	// ����0��ʾ��ȡ�ɹ�
	// ѭ�����ô˺��������Ի�ȡ�������ļ���Ϣ,����1˵����ȡ���
	public native int nGetSdFileName(FileResInfo file, int from_first);

	// ����ָ���ն˲���SD�ļ��Ĳ���״̬
	// tmid �ն�id
	// play_sta 0:ֹͣ���ţ�1�����ţ�2����ͣ����
	// �ɹ�����0
	public native int nCtrlSdFile(int tmid, int play_sta);

	// ���ư��ն˶Խ�
	// rqtype �Խ���������
	// 1 ��ʾSDK���ն��������������նˣ�
	// 2 ��ʾSDK���ն������Ͽ���ǰ�Խ���
	// 3 ��ʾSDK���ն��������������ն˺��У�
	// call_tmid �Խ�ʱ�Է��ն�ID��
	// call_tm_f
	// 1��SDK���ն��������������ն�ʱ�����������ն˵ķֻ���(���ն����Ӷ��������壬ÿ����嶼�ж����ĵ�ַ���1-15),����Ϊ0��
	// 2����Rqtype=3ʱCallTmFΪ0�����ܺ��У�1����
	// �ɹ�����0
	public native int nCtrlBandTmTalk(int rqtype, int call_tmid, int call_tm_f);

	// ��ȡSDK�汾��
	// 2�����ֽڱ�ʾ���汾��2�����ֽڱ�ʾ�ΰ汾
	public native int nGetSdkVer();

	// �������������ն˺���
	// main_tmid �����ն�ID
	// other_tmid �����ն�ID
	// other_tmf �����ն˵ķֻ���
	// �ɹ�����0
	public native int nCtrlAnyTmForCall(int main_tmid, int other_tmid, int other_tmf);

	// ���Ƶ�ǰ�ն˶Ͽ����жԽ����ͨ���жԽ�
	// any_tmid Ҫ���Ƶ��ն�ID
	// tsta 1Ϊ��ͨ�Խ���0Ϊ�Ͽ��Խ������
	// �ɹ�����0
	public native int nCtrlAnyTmTalkSta(int any_tmid, int tsta);

	// �����ն�����
	// any_tmid Ҫ���Ƶ��ն�ID
	// tvol �ն�����(1-100)
	// �ɹ�����0
	public native int nCtrlAnyTmVol(int any_tmid, int tvol);

	// �����ն��ֳ�
	// main_tmid ���������ն�ID
	// other_tmid �������ն�ID
	// other_tmf �������ն˵ķֻ���
	// �ɹ�����0
	public native int nMonitorAnyTm(int main_tmid, int other_tmid, int other_tmf);

	// �˳������ն��ֳ�
	// tmid ������������ն�ID
	// �ɹ�����0
	public native int nMonitorExitAnyTm(int tmid);

	// ���ư��ն˶Խ�ʱ���Է�ת�ӵ������ն�
	// other_tmid ת�ӵ������ն�ID
	// call_exten_type ת���ͣ�
	// 0 �����κβ���ת��
	// 1 ���ֻ��Ų���ת��
	// 2 ���绰����ת��(�˲���ʱotherTmid����Ϊ�绰�ն�)
	// other_tmf ת���ն˵ķֻ���
	// tel_no ת�ն˵ĵ绰����(ע��绰��������ASC��)
	// �ɹ�����0
	public native int nCtrlBandTmCallForWard(int other_tmid, int call_exten_type, int other_tmf, byte[] tel_no);

	// �ֶ����������ն�״̬
	// ����SDK������ʵʱ�����ն�״̬��������Ҫ����ĵõ�ʵʱ���ն�״̬�ɵ��ô˽ӿ�,ÿ����һ�θ��µ�ǰ�ն˵�״̬
	public native void nCtrlUpdateTmSta();

	// ����ϵͳ���������ӿ�������ѡ��˻����ӿ�ΪϵͳĬ�Ͻӿ�
	// cap_mix_name ��ǰҪ���õ����������ӿ���
	// set_type ���ô˻����ӿ�����������ΪϵͳĬ�Ͻӿڣ�SetType =1Ϊ����������SetType =2Ϊ����ϵͳĬ�Ͻӿ�
	// mval ���ô˻����ӿ�����ֵ(0-100)
	public native void nSetSysSoundCardMix(String cap_mix_name, int set_type, int mval);

	// ���캯��
	public IPAVH() {
		bInit = false;
		bConnect = false;
		init();
	}

	// ��ʼ��
	public void init() {
		if (!bInit) {
			bInit = true;
			bConnect = false;

			// ���ض�̬��
			System.loadLibrary("IPAVHWrapper");

			// ��ʼ��sdk
			nInit();

			// ���ûص��ӿڶ���
			cb = new IPAVHCB();
			cb.SetPaernt(this);
			nSetUserCallBack(cb);
		}
	}

	// ����
	public void cleanup() {
		if (bInit) {
			bInit = false;
			bConnect = false;

			nResetUserCallBack(cb);

			// SDK�˳�����
			nCleanup();
		}
	}

	// �ص������������������д
	// ��½״̬�ص�����
	// is_con sdk֪ͨ����״̬����ֵΪFALSE�ѶϿ�
	public void connectOrDis(int is_con, UserInfo user_info) {
		if (is_con == 0) {
			bConnect = false;
			System.out.println("�Ͽ��������������......");
		} else {
			bConnect = true;
			System.out.println("��¼�ɹ�......");
		}
	}

	// �����㲥�ص�����
	public void newGbSta(int stream_id, int is_new, int is_new_ok) {
		if (is_new != 0) // Ϊ�´����Ĺ㲥����������Ӧ�Ƿ񴴽��ɹ�
		{
			if (is_new_ok != 0) {
				System.out.println("�㲥�����ɹ�......");
			} else {
				System.out.println("�㲥����ʧ��......");
			}
		} else // �˹㲥ǰ���Ѵ����ɹ������˹㲥��ֹͣ
		{
			System.out.println("�㲥��ֹͣ......");
		}
	}

	// �ն�״̬�ص�����
	public void terminalSta(TMInfo tm_info) {
		System.out.println("�ն� " + tm_info.name + " ״̬�ı�,�µ�״ֵ̬Ϊ: " + tm_info.status);
	}

	// �ն˴���32����չ����֪ͨ�ص�����
	public void terminalKeySta(int tmid, int key_val) {
		System.out.println("�ն˴���32����չ����֪ͨ�ص�");
	}

	// ����״̬�ص�����
	// no �������ź�����32·�����ɼ��ն˻���(1-32) Fno=1 ��ʾ1-32�ź�,Fno=2 ��ʾ33-64�ź�,��������
	// val 32·�ź�״̬(���ֵĵ�λΪ��һ·�ź�),λΪ1��ʾ���źţ�λ0���ź�
	public void fireSta(int no, int val) {
		System.out.println("����״̬�ص�,�ն˺�: " + no + "״̬: " + val);
	}

	// �ն˱����ص�����
	// tmid �����ն�ID
	// arm_sta ��������
	public void TmArmSta(int tmid, int arm_sta) {
		System.out.println("�ն˱����ص�,�ն˺�: " + tmid + "��������: " + arm_sta);
	}

	// ���ն˶Խ�״̬�ص�����
	// call_sta �Խ�״̬
	// 1 ��ʾ���������ն˺���SDK���ն�
	// 2 ��ʾSDK���ն��������ն����ڶԽ�
	// 3 ��ʾSDK���ն˺��������նˣ��Է������ܺ���
	// 4 ��ʾSDK���ն˺��������ն�ʧ��
	// 6 ��ʾ���������ն˺���SDK���ն��ѽ�����ж���(ע���ʱ��û�ж԰��ն˽���ʵ���Ժ������̣�ֻ�Ǽ�����ж��еȴ������������)
	// call_tmid �Խ�ʱ�Է��ն�ID��
	// call_mbid �Խ�ʱ�Է��ն˷ֻ���
	public void bandTmTalkSta(int call_sta, int call_tmid, int call_mbid) {
		System.out.println("���ն˶Խ�״̬�ص�,�Խ�״̬: " + call_sta + "�Խ�ʱ�Է��ն�ID��: " + call_tmid + "�Խ�ʱ�Է��ն˷ֻ���: " + call_mbid);
	}

	// ˽�г�Ա����
	private boolean bInit; // �Ƿ��ʼ��
	protected boolean bConnect; // �Ƿ����ӷ�����
	private IPAVHCB cb;
}
