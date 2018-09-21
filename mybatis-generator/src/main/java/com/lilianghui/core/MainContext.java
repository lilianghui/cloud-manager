package com.lilianghui.core;

import com.lilianghui.ui.MainFrame;
import com.lilianghui.ui.SettingFrame;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainContext extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5513101765605790332L;
	public SettingFrame settingFrame = new SettingFrame(this);
	public MainFrame mainFrame = new MainFrame(this);
	public CardLayout cardLayout = new CardLayout(); // 创建卡片布局的对象
	public JPanel mainJPanel = new JPanel(); // 默认布局是 流布局

	private int width = 1100;
	private int height = 650;

	public MainContext() {
		this.setTitle("MyBatis类生成工具");
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(width, height);
		this.setBounds((d.width - width) / 2, (d.height - height) / 2, width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		this.setVisible(true);

		Container c = this.getContentPane();// 在这里获取容器面板
		mainJPanel.setLayout(cardLayout);// 重新设置面板的布局 为卡片布局

		mainJPanel.add(settingFrame, "one"); // 必须指定 标识符 ，如果没有标识符
		mainJPanel.add(mainFrame, "two");// 在mainJpanel添加控件
		c.add(mainJPanel, BorderLayout.CENTER); // 把面板对象，添加到容器的中间部分

		this.setVisible(true);// 设置窗体可见
	}

	public void previous() {
		cardLayout.previous(mainJPanel);

	}

	public MainFrame next() {
		cardLayout.next(mainJPanel);
		return mainFrame;
	}

	public static void main(String[] args) {
		new MainContext();
	}

}
