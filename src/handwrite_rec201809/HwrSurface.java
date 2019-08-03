package handwrite_rec201809;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * 手写识别界面类，主要是创建界面和添加监听器
 * 
 * @author Poe
 *
 */
public class HwrSurface extends JPanel implements ActionListener, MouseListener, MouseMotionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static final int width = 8;// 格子宽
	static final int ROW_NUM = 60;// 格子列数
	static final int COL_NUM = 80;// 格行数
	private boolean do_recoginize = false;
	private Graphics hwr_graphics;
	private JFrame jframe_window;
	private JPanel show_recoginion;
	private JButton[] rec_but;
	private JTextArea input;
	private boolean train = false;
	private String train_character;
	private JButton add_data;

	private int x1, y1, x2, y2;
	private int[][] flag;// 用于存储1，0

	// 构造函数
	public HwrSurface() {
		jframe_window = new JFrame();
		jframe_window.setSize(1000, 618);
		jframe_window.setTitle("手写识别");
		jframe_window.setDefaultCloseOperation(3);
		jframe_window.setLocationRelativeTo(null);
		jframe_window.setLayout(new FlowLayout());
		Dimension size_dim = new Dimension(width * COL_NUM, width * ROW_NUM);
		this.setPreferredSize(size_dim);
		this.setBackground(Color.pink);
		jframe_window.add(this);

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		flag = new int[ROW_NUM][COL_NUM];

		show_recoginion = new JPanel();// 用于显示识别出的字符
		show_recoginion.setLayout(new FlowLayout());
		show_recoginion.setPreferredSize(new Dimension(width * COL_NUM, 30));
		show_recoginion.setBackground(Color.white);
		jframe_window.add(show_recoginion);
		// 向show_recoginion里面添加识别出的字符
		rec_but = new JButton[Recoginize.CHAR_NUM];
		for (int i = 0; i < rec_but.length; i++) {
			rec_but[i] = new JButton();
			rec_but[i].setPreferredSize(new Dimension(width * COL_NUM / (Recoginize.CHAR_NUM+1), 25));
			rec_but[i].setBorder(null);
			rec_but[i].setBackground(Color.white);
			rec_but[i].setFocusable(false);
			rec_but[i].addActionListener(this);
			show_recoginion.add(rec_but[i]);
		}

		input = new JTextArea();// 用于输入字符
		input.setPreferredSize(new Dimension(width * COL_NUM - 195, 30));
		input.setBackground(Color.white);
		jframe_window.add(input);

		JButton chose_button = new JButton("重写");
		chose_button.setFocusable(false);
		chose_button.addActionListener(this);
		jframe_window.add(chose_button);

		jframe_window.setVisible(true);
		hwr_graphics = this.getGraphics();
		hwr_graphics.setColor(Color.BLACK);

		add_data = new JButton("训练");// 加入训练集
		add_data.setFocusable(false);
		jframe_window.add(add_data);
		add_data.addActionListener(this);
		
		JButton imgBtn = new JButton("图片");// 图片识别
		imgBtn.setFocusable(false);
		jframe_window.add(imgBtn);
		imgBtn.addActionListener(this);

	}

	public JButton[] getRec_but() {
		return rec_but;
	}

	// 画线 28*16的大小
	public void paint(Graphics gra) {
		super.paint(gra);
		gra.setColor(Color.BLACK);
		for (int i = 0; i < flag.length; i++) {// 重置数组
			for (int j = 0; j < flag[0].length; j++) {
				flag[i][j] = 0;
			}
		}
	}

	// 动作监听器
	@Override
	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand();
		if (command == "训练") {// 将该数据加入到训练集
			String option = JOptionPane.showInputDialog("请输入要训练的字符");
			train_character=option;//要训练的字符
			if (option != null) {
				JOptionPane.showMessageDialog(jframe_window, "请在写字板中写字，写完后移开写字板，每写完一个字生成一个样本，训练结束后请点击停止按钮，返回到字符识别", "提示", JOptionPane.INFORMATION_MESSAGE);
				train=true;
				add_data.setText("停止");
			}
		} else if (command == "重写") {// 选中的是识别出的字符
			this.repaint();// 重绘
			for (int i = 0; i < flag.length; i++) {// 重置数组
				for (int j = 0; j < flag[0].length; j++) {
					flag[i][j] = 0;
				}
			}

		}else if (command == "图片") {// 选中的是识别出的字符
			new PicChose(rec_but);
		} else if (command == "停止") {// 选中的是识别出的字符
			train=false;
			add_data.setText("训练");
		} 
		else {// 选中识别字符
			input.append(command);
			this.repaint();// 重绘
			for (int i = 0; i < flag.length; i++) {// 重置数组
				for (int j = 0; j < flag[0].length; j++) {
					flag[i][j] = 0;
				}
			}
		}

	}

	// 鼠标监听器
	@Override
	public void mouseClicked(MouseEvent arg0) {
		x1 = arg0.getX();
		y1 = arg0.getY();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		do_recoginize = false;
	}

	// 鼠标移出画板开始识别
	@Override
	public void mouseExited(MouseEvent arg0) {
		if (do_recoginize&&train) {//训练
			Recoginize rec = new Recoginize(flag);
			rec.generatData(train_character);
			int[][] flagup=flag;
			int[][] flagdown=flag;
			int[][] flagleft=flag;
			int[][] flagright=flag;
			stop1:
				while(true) {
					for(int i=0;i<flagup.length;i++) {
						for(int j=0;j<flagup[0].length;j++) {
							if(flagup[i][j]==1&&i-3>0) {
								flagup[i-3][j]=flagup[i][j];
								flagup[i][j] = 0;
							}else if(flagup[i][j]==1&&i-3<=0) {
								break stop1;
							}
						}
					}
					rec.setFlag(flagup);
					rec.generatData(train_character);
				}
			
			stop2:
				while(true) {
					for(int i=flagdown.length-1;i>=0;i--) {
						for(int j=0;j<flagdown[0].length;j++) {
							if(flagdown[i][j]==1&&i+3<flagdown.length) {
								flagdown[i+3][j]=flagdown[i][j];
								flagdown[i][j] = 0;
							}else if(flagdown[i][j]==1&&i+3>=flagdown.length) {
								break stop2;
							}
						}
					}
					rec.setFlag(flagdown);
					rec.generatData(train_character);
				}
				
			stop3:
				while(true) {
					for(int i=0;i<flagleft.length;i++) {
						for(int j=0;j<flagleft[0].length;j++) {
							if(flagleft[i][j]==1&&j-3>0) {
								flagleft[i][j-3]=flagleft[i][j];
								flagleft[i][j] = 0;
							}else if(flagleft[i][j]==1&&j-3<=0) {
								break stop3;
							}
						}
					}
					rec.setFlag(flagleft);
					rec.generatData(train_character);
				}
			stop4:
				while(true) {
					for(int i=0;i<flagright.length;i++) {
						for(int j=flagright[0].length-1;j>=0;j--) {
							if(flagright[i][j]==1&&j+3<flagright[0].length) {
								flagright[i][j+3]=flagright[i][j];
								flagright[i][j] = 0;
							}else if(flagright[i][j]==1&&j+3>=flagright[0].length) {
								break stop4;
							}
						}
					}
					rec.setFlag(flagright);
					rec.generatData(train_character);
				}
	
		}

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		x1 = arg0.getX();
		y1 = arg0.getY();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if(!train){
			Recoginize rec = new Recoginize(flag);
			char[] rec_result = rec.getChars_result();// 获得结果
			// 向show_recoginion中添加识别出的字符
			for (int i = 0; i < rec_result.length; i++) {
				rec_but[i].setText("" + rec_result[i]);
			}
		}

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Graphics2D g2d = (Graphics2D) hwr_graphics;
		BasicStroke stokeLine = new BasicStroke(5.0f);
		g2d.setStroke(stokeLine);
		x2 = e.getX();
		y2 = e.getY();
		g2d.drawLine(x1, y1, x2, y2);
		x1 = x2;
		y1 = y2;
		int index_x = x1 / width;
		int index_y = y1 / width;
		if (index_x >= COL_NUM)
			index_x = COL_NUM-1;
		if (index_y >= ROW_NUM)
			index_y = ROW_NUM-1;
		if (index_x < 0)
			index_x = 0;
		if (index_y < 0)
			index_y = 0;
		flag[index_y][index_x] = 1;
		do_recoginize = true;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
