package sdk;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class IPAVHHandler extends IPAVH {
	public void SetFrame(IPAVHFrame f) {
		frame = f;
	}

	public IPAVHFrame GetFrame() {
		return frame;
	}

	// �ص�����
	public void connectOrDis(int is_con, UserInfo user_info) {
		if (is_con == 0) {
			bConnect = false;
			frame.textArea.append("�Ͽ��������������......\n");

			frame.InitButtonsEnabledStatus();
		} else {
			bConnect = true;

			frame.SetEnableLogin(false);
			frame.SetEnableSendGb(true);

			frame.textArea.append("��¼�ɹ�......\n");
		}
	}

	public void newGbSta(int stream_id, int is_new, int is_new_ok) {
		if (is_new != 0) // Ϊ�´����Ĺ㲥����������Ӧ�Ƿ񴴽��ɹ�
		{
			if (is_new_ok != 0) {
				frame.SetEnableSendGb(false);

				frame.textArea.append("�㲥�����ɹ�......\n");
			} else {
				frame.textArea.append("�㲥����ʧ��......\n");
			}
		} else // �˹㲥ǰ���Ѵ����ɹ������˹㲥��ֹͣ
		{
			frame.SetEnableSendGb(true);

			frame.textArea.append("�㲥��ֹͣ......\n");
		}
	}

	private IPAVHFrame frame;
}

class IPAVHFrame extends Frame {
	// ���캯��
	IPAVHFrame() {
		nCurGBStreamId = 0;

		setTitle("Java IP����㲥�ͻ��˲��Գ���");
		setLocation(100, 100);
		setSize(700, 550);

		// ��ʹ�ò���
		setLayout(null);

		Label lb = new Label("�û���");
		lb.setBounds(20, 40, 50, 20);
		add(lb);

		tfUserName = new TextField("1");
		tfUserName.setBounds(100, 40, 100, 20);
		add(tfUserName);

		lb = new Label("����");
		lb.setBounds(20, 70, 50, 20);
		add(lb);

		tfPassword = new TextField("2");
		tfPassword.setBounds(100, 70, 100, 20);
		add(tfPassword);

		lb = new Label("������IP");
		lb.setBounds(220, 40, 50, 20);
		add(lb);

		tfIP = new TextField("127.0.0.1");
		tfIP.setBounds(300, 40, 100, 20);
		add(tfIP);

		lb = new Label("�������˿ں�");
		lb.setBounds(220, 70, 50, 20);
		add(lb);

		tfPort = new TextField("3960");
		tfPort.setBounds(300, 70, 100, 20);
		add(tfPort);

		buttonLogin = new Button("��¼");
		buttonLogin.setBounds(420, 50, 100, 30);
		add(buttonLogin);

		buttonLogOut = new Button("�˳�");
		buttonLogOut.setBounds(570, 50, 100, 30);
		add(buttonLogOut);

		buttonFileGB = new Button("�ļ��㲥");
		buttonFileGB.setBounds(20, 120, 100, 30);
		add(buttonFileGB);

		buttonSoundCardGB = new Button("�����㲥");
		buttonSoundCardGB.setBounds(185, 120, 100, 30);
		add(buttonSoundCardGB);

		buttonTerminalGB = new Button("���ն˲ɲ�");
		buttonTerminalGB.setBounds(350, 120, 100, 30);
		add(buttonTerminalGB);

		buttonTTSGB = new Button("TTS�ı�ת�����㲥");
		buttonTTSGB.setBounds(515, 120, 150, 30);
		add(buttonTTSGB);

		buttonStopGB = new Button("ֹͣ�㲥");
		buttonStopGB.setBounds(350, 160, 100, 30);
		add(buttonStopGB);

		InitButtonsEnabledStatus();

		textArea = new TextArea();
		textArea.setBounds(20, 200, 660, 330);
		textArea.setEditable(false);
		add(textArea);

		setVisible(true);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// �����˳�ʱ����
				handler.cleanup();

				System.exit(0);
			}
		});

		// ��¼�����¼�
		buttonLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ��¼
				String str_name = tfUserName.getText();
				String str_password = tfPassword.getText();
				String str_ip = tfIP.getText();
				int port = Integer.parseInt(tfPort.getText());
				if (handler.nLogInByIP(str_name, str_password, str_ip, port, true, false, 0) != 0) {
					textArea.append("��¼ʧ��......\n");
				}
			}
		});

		// �Ͽ������¼�
		buttonLogOut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// �Ͽ��������������
				handler.nLogOut();
			}
		});

		// ֹͣ�㲥�¼�
		buttonStopGB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ɾ���㲥��
				if (handler.nDelGbStream(nCurGBStreamId) == 0) {
					textArea.append("ɾ���㲥......\n");

					SetEnableSendGb(true);
				}
			}
		});

		// �����ļ��㲥�¼�
		buttonFileGB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ��ȡ�������ļ���Դ
				FileResInfo file = new FileResInfo();
				if (handler.nGetFileRes(file, 1) == 0) {
					System.out.println(file.file_dir);
					System.out.println(file.file_name);

					FileResInfo[] files = new FileResInfo[1];
					files[0] = file;

					int[] tms = new int[1];
					tms[0] = 1;

					// �����ļ��㲥
					nCurGBStreamId = handler.nCreateNewGbByFile(files, tms, false);
					if (nCurGBStreamId != 0) {
						textArea.append("�ļ��㲥��id=" + nCurGBStreamId + "\n");
					}
				}
			}
		});

		// ���������㲥�¼�
		buttonSoundCardGB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// ��ȡ������Ϣ
				String[] card_list = handler.nGetSysSoundCardInfo();
				if (card_list.length > 0) {
					System.out.println("������" + card_list[0]);

					int[] tms = new int[1];
					tms[0] = 1;

					// ���������㲥
					nCurGBStreamId = handler.nCreateNewGbBySoundCard(card_list[0], tms, 1, 1);
					if (nCurGBStreamId != 0) {
						textArea.append("�����㲥��id=" + nCurGBStreamId + "\n");
					}
				}
			}
		});

		// ���ն˲ɲ�
		buttonTerminalGB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] tms = new int[1];
				tms[0] = 1;

				// �����ļ��㲥
				nCurGBStreamId = handler.nCreateNewGbByTerminal(tms, 1, 1);
				if (nCurGBStreamId != 0) {
					textArea.append("���ն˲ɲ��㲥��id=" + nCurGBStreamId + "\n");
				}
			}
		});

		// TTS�ı�ת�����㲥
		buttonTTSGB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] tms = new int[1];
				tms[0] = 1;

				// ����TTS�ı�ת�����㲥
				String text = "�����ı�ת�����㲥";
				nCurGBStreamId = handler.nCreateNewGbByTTS(text, 1, 2, tms);
				if (nCurGBStreamId != 0) {
					textArea.append("TTS�ı�ת�����㲥��id=" + nCurGBStreamId + "\n");
				}
			}
		});
	}

	public void SetHandler(IPAVHHandler h) {
		handler = h;
	}

	public IPAVHHandler GetHandler() {
		return handler;
	}

	public void SetEnableSendGb(boolean enable) {
		buttonFileGB.setEnabled(enable);
		buttonSoundCardGB.setEnabled(enable);
		buttonTerminalGB.setEnabled(enable);
		buttonTTSGB.setEnabled(enable);

		buttonStopGB.setEnabled(!enable);
	}

	public void SetEnableLogin(boolean enable) {
		buttonLogin.setEnabled(enable);
		buttonLogOut.setEnabled(!enable);
	}

	public void InitButtonsEnabledStatus() {
		buttonLogin.setEnabled(true);
		buttonLogOut.setEnabled(false);

		buttonFileGB.setEnabled(false);
		buttonSoundCardGB.setEnabled(false);
		buttonTerminalGB.setEnabled(false);
		buttonTTSGB.setEnabled(false);

		buttonStopGB.setEnabled(false);
	}

	// ��Ա����
	public Button buttonLogin; // �û���¼��ť
	public Button buttonLogOut; // �û��Ͽ���ť
	public Button buttonFileGB; // �ļ��㲥��ť
	public Button buttonSoundCardGB; // �����㲥��ť
	public Button buttonTerminalGB; // ���ն˲ɲ�
	public Button buttonTTSGB; // TTS�ı�ת�����㲥
	public Button buttonStopGB; // ֹͣ�㲥��ť
	public TextArea textArea; // �㲥��־����
	public TextField tfUserName;
	public TextField tfPassword;
	public TextField tfIP;
	public TextField tfPort;

	public int nCurGBStreamId;

	private IPAVHHandler handler;

	// ������
	public static void main(String args[]) {
		IPAVHFrame frame = new IPAVHFrame();
		IPAVHHandler handler = new IPAVHHandler();

		frame.SetHandler(handler);
		handler.SetFrame(frame);
	}
}