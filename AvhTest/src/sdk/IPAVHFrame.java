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

	// 回调函数
	public void connectOrDis(int is_con, UserInfo user_info) {
		if (is_con == 0) {
			bConnect = false;
			frame.textArea.append("断开与服务器的连接......\n");

			frame.InitButtonsEnabledStatus();
		} else {
			bConnect = true;

			frame.SetEnableLogin(false);
			frame.SetEnableSendGb(true);

			frame.textArea.append("登录成功......\n");
		}
	}

	public void newGbSta(int stream_id, int is_new, int is_new_ok) {
		if (is_new != 0) // 为新创建的广播，服务器回应是否创建成功
		{
			if (is_new_ok != 0) {
				frame.SetEnableSendGb(false);

				frame.textArea.append("广播创建成功......\n");
			} else {
				frame.textArea.append("广播创建失败......\n");
			}
		} else // 此广播前面已创建成功，但此广播已停止
		{
			frame.SetEnableSendGb(true);

			frame.textArea.append("广播已停止......\n");
		}
	}

	private IPAVHFrame frame;
}

class IPAVHFrame extends Frame {
	// 构造函数
	IPAVHFrame() {
		nCurGBStreamId = 0;

		setTitle("Java IP网络广播客户端测试程序");
		setLocation(100, 100);
		setSize(700, 550);

		// 不使用布局
		setLayout(null);

		Label lb = new Label("用户名");
		lb.setBounds(20, 40, 50, 20);
		add(lb);

		tfUserName = new TextField("1");
		tfUserName.setBounds(100, 40, 100, 20);
		add(tfUserName);

		lb = new Label("密码");
		lb.setBounds(20, 70, 50, 20);
		add(lb);

		tfPassword = new TextField("2");
		tfPassword.setBounds(100, 70, 100, 20);
		add(tfPassword);

		lb = new Label("服务器IP");
		lb.setBounds(220, 40, 50, 20);
		add(lb);

		tfIP = new TextField("127.0.0.1");
		tfIP.setBounds(300, 40, 100, 20);
		add(tfIP);

		lb = new Label("服务器端口号");
		lb.setBounds(220, 70, 50, 20);
		add(lb);

		tfPort = new TextField("3960");
		tfPort.setBounds(300, 70, 100, 20);
		add(tfPort);

		buttonLogin = new Button("登录");
		buttonLogin.setBounds(420, 50, 100, 30);
		add(buttonLogin);

		buttonLogOut = new Button("退出");
		buttonLogOut.setBounds(570, 50, 100, 30);
		add(buttonLogOut);

		buttonFileGB = new Button("文件广播");
		buttonFileGB.setBounds(20, 120, 100, 30);
		add(buttonFileGB);

		buttonSoundCardGB = new Button("声卡广播");
		buttonSoundCardGB.setBounds(185, 120, 100, 30);
		add(buttonSoundCardGB);

		buttonTerminalGB = new Button("绑定终端采播");
		buttonTerminalGB.setBounds(350, 120, 100, 30);
		add(buttonTerminalGB);

		buttonTTSGB = new Button("TTS文本转语音广播");
		buttonTTSGB.setBounds(515, 120, 150, 30);
		add(buttonTTSGB);

		buttonStopGB = new Button("停止广播");
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
				// 程序退出时清理
				handler.cleanup();

				System.exit(0);
			}
		});

		// 登录处理事件
		buttonLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 登录
				String str_name = tfUserName.getText();
				String str_password = tfPassword.getText();
				String str_ip = tfIP.getText();
				int port = Integer.parseInt(tfPort.getText());
				if (handler.nLogInByIP(str_name, str_password, str_ip, port, true, false, 0) != 0) {
					textArea.append("登录失败......\n");
				}
			}
		});

		// 断开处理事件
		buttonLogOut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 断开与服务器的连接
				handler.nLogOut();
			}
		});

		// 停止广播事件
		buttonStopGB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 删除广播流
				if (handler.nDelGbStream(nCurGBStreamId) == 0) {
					textArea.append("删除广播......\n");

					SetEnableSendGb(true);
				}
			}
		});

		// 发送文件广播事件
		buttonFileGB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 获取服务器文件资源
				FileResInfo file = new FileResInfo();
				if (handler.nGetFileRes(file, 1) == 0) {
					System.out.println(file.file_dir);
					System.out.println(file.file_name);

					FileResInfo[] files = new FileResInfo[1];
					files[0] = file;

					int[] tms = new int[1];
					tms[0] = 1;

					// 发送文件广播
					nCurGBStreamId = handler.nCreateNewGbByFile(files, tms, false);
					if (nCurGBStreamId != 0) {
						textArea.append("文件广播流id=" + nCurGBStreamId + "\n");
					}
				}
			}
		});

		// 发送声卡广播事件
		buttonSoundCardGB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 获取声卡信息
				String[] card_list = handler.nGetSysSoundCardInfo();
				if (card_list.length > 0) {
					System.out.println("声卡：" + card_list[0]);

					int[] tms = new int[1];
					tms[0] = 1;

					// 发送声卡广播
					nCurGBStreamId = handler.nCreateNewGbBySoundCard(card_list[0], tms, 1, 1);
					if (nCurGBStreamId != 0) {
						textArea.append("声卡广播流id=" + nCurGBStreamId + "\n");
					}
				}
			}
		});

		// 绑定终端采播
		buttonTerminalGB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] tms = new int[1];
				tms[0] = 1;

				// 发送文件广播
				nCurGBStreamId = handler.nCreateNewGbByTerminal(tms, 1, 1);
				if (nCurGBStreamId != 0) {
					textArea.append("绑定终端采播广播流id=" + nCurGBStreamId + "\n");
				}
			}
		});

		// TTS文本转语音广播
		buttonTTSGB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] tms = new int[1];
				tms[0] = 1;

				// 发送TTS文本转语音广播
				String text = "测试文本转语音广播";
				nCurGBStreamId = handler.nCreateNewGbByTTS(text, 1, 2, tms);
				if (nCurGBStreamId != 0) {
					textArea.append("TTS文本转语音广播流id=" + nCurGBStreamId + "\n");
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

	// 成员变量
	public Button buttonLogin; // 用户登录按钮
	public Button buttonLogOut; // 用户断开按钮
	public Button buttonFileGB; // 文件广播按钮
	public Button buttonSoundCardGB; // 声卡广播按钮
	public Button buttonTerminalGB; // 绑定终端采播
	public Button buttonTTSGB; // TTS文本转语音广播
	public Button buttonStopGB; // 停止广播按钮
	public TextArea textArea; // 广播日志文字
	public TextField tfUserName;
	public TextField tfPassword;
	public TextField tfIP;
	public TextField tfPort;

	public int nCurGBStreamId;

	private IPAVHHandler handler;

	// 主函数
	public static void main(String args[]) {
		IPAVHFrame frame = new IPAVHFrame();
		IPAVHHandler handler = new IPAVHHandler();

		frame.SetHandler(handler);
		handler.SetFrame(frame);
	}
}