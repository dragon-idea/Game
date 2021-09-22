package 游戏界面;

import 客户端与服务端.Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Login_GUI extends JFrame implements ActionListener
{
	String ip = "127.0.0.1";
	String imagePath = "./src/图片/资源包/res/face/1-1.gif";
	//北区
	JLabel tip_lable = new JLabel("请输入您的个人信息：");
	JLabel name_lable = new JLabel("用户名：");
	JLabel server_lable = new JLabel("服务器：");
	JLabel image_lable = new JLabel("头   像：");
	
	JTextField name_text = new JTextField();
	JTextField server_text = new JTextField(ip);
	JLabel userImage_lable = new JLabel(new ImageIcon(imagePath), JLabel.LEFT);
	
	//中区
	JButton[] image_btn = new JButton[86];
	
	//南区
	JButton connet_btn = new JButton("连接");
	JButton reset_btn = new JButton("重置");
	JButton exit_btn = new JButton("退出");
	
	public Login_GUI()
	{
		JPanel North = new JPanel();
		JPanel Center = new JPanel();
		JPanel South = new JPanel();
		
		//北区
		JPanel North_west = new JPanel();
		JPanel North_center = new JPanel();
		
		North_west.setBackground(Color.WHITE);
		North_west.setLayout(new GridLayout(3,1, 0, 5));
		North_west.add(name_lable);
		North_west.add(server_lable);
		North_west.add(image_lable);
		
		North_center.setBackground(Color.WHITE);
		North_center.setLayout(new GridLayout(3,1, 0, 5));
		North_center.add(name_text);
		North_center.add(server_text);
		North_center.add(userImage_lable);
		
		North.setBackground(Color.WHITE);
		North.setLayout(new BorderLayout());
		North.add(tip_lable, "North");
		North.add(North_west, "West");
		North.add(North_center, "Center");
		
		//中区
		Center.setLayout(null);
		Center.setBackground(Color.WHITE);
		int num = 0;
//		add(new JScrollPane(image_area));
		
		for (int y = 0; y < 8; y++)
		{
			int width = 40;
			for (int x = 0; x < 12; x++)
			{
				if(num < 85)
				{
					String path = "./src/图片/资源包/res/face/" + (num + 1) + "-1.gif";
					image_btn[num] = new JButton(new ImageIcon(path));
					image_btn[num].setBounds(width * x, width * y, width, width);
					Center.add(image_btn[num]);
					
					image_btn[num].setBackground(Color.WHITE);
					image_btn[num].addActionListener(this);
					num++;
				}
			}
		}
		
		//南区
		South.add(connet_btn);
		South.add(reset_btn);
		South.add(exit_btn);
		South.setBackground(Color.WHITE);
		
		exit_btn.addActionListener(this);
		reset_btn.addActionListener(this);
		connet_btn.addActionListener(this);
		exit_btn.setBackground(Color.WHITE);
		reset_btn.setBackground(Color.WHITE);
		connet_btn.setBackground(Color.WHITE);
		
		setLayout(new BorderLayout(0, 5));
		add(North, "North");
		add(Center, "Center");
		add(South, "South");
		
		setTitle("登陆界面");
		setVisible(true);
		setSize(500, 530);
		setLocation(700, 300);
		setBackground(Color.WHITE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}

	public void actionPerformed(ActionEvent e)
	{
		JButton button = (JButton)e.getSource();
		
		for(int i = 0; i < 85; i++)
		{
			if (button == image_btn[i])
			{
				Icon icon = button.getIcon();
				userImage_lable.setIcon(icon);
				imagePath =  "./src/图片/资源包/res/face/" + (i + 1) + "-1.gif";
			}
		}
		
		if (button == reset_btn)
		{
			name_text.setText("");
			server_text.setText("127.0.0.1");
			userImage_lable.setIcon(null);
		}
		
		if (button == exit_btn)
		{
			int result = JOptionPane.showConfirmDialog(null, "确认退出?", "确认", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if(result == JOptionPane.OK_OPTION)
                System.exit(0);
		}
		
		if (button == connet_btn)
		{
			if(name_text.getText().equals(""))
				JOptionPane.showMessageDialog(null, "请输入名字！", "错误", JOptionPane.ERROR_MESSAGE);
			else if(!server_text.getText().equals("127.0.0.1"))
				JOptionPane.showMessageDialog(null, "请输入ip：127.0.0.1！", "错误", JOptionPane.ERROR_MESSAGE);
			else if(userImage_lable.getIcon() == null)
				JOptionPane.showMessageDialog(null, "请选择一个头像！", "错误", JOptionPane.ERROR_MESSAGE);
			else
			{
				String name = name_text.getText();
				Client client = new Client();
				GameHall_GUI gh = new GameHall_GUI(name, imagePath, client);
				client.connectServer();
				client.setUserInfo(name, imagePath);
				
				this.setVisible(false);															//本窗口隐藏,
				gh.Display();
				client.sendMSG("OK");
				this.dispose();																		//本窗口销毁,释放内存资源
			}
			
		}
		
		
	}
	
	public static void main(String[] args) 
	{
		new Login_GUI();
	}
	
}


