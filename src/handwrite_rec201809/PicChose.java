package handwrite_rec201809;


import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
 
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
 
public class PicChose extends JFrame implements ActionListener
{
 
    private static final long serialVersionUID = 1L;
    private String path;
    private JButton[] rec_but;
 
    JButton btn = null;
    JButton btnConfirm = null;
 
    JTextField textField = null;
    boolean confirm=false;
 
    public PicChose(JButton[] rec_but)
    {
    	this.rec_but=rec_but;
        this.setTitle("选择文件窗口");
        FlowLayout layout = new FlowLayout();// 布局
        JLabel label = new JLabel("请选择文件：");// 标签
        textField = new JTextField(30);// 文本域
        btn = new JButton("浏览");// 钮1
        btnConfirm = new JButton("确定");// 钮1
        // 设置布局
        layout.setAlignment(FlowLayout.LEFT);// 左对齐
        this.setLayout(layout);
        this.setBounds(400, 200, 600, 70);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(2);
        btn.addActionListener(this);
        btnConfirm.addActionListener(this);
        this.add(label);
        this.add(textField);
        this.add(btn);
        this.add(btnConfirm);
 
    }
    
    public boolean isConfirm() {
		return confirm;
	}

	public JTextField getTextField() {
		return textField;
	}

	public JButton getBtnConfirm() {
		return btnConfirm;
	}

	@Override
    public void actionPerformed(ActionEvent e)
    {
    	if(e.getActionCommand().equals("浏览")){
	        JFileChooser chooser = new JFileChooser();
	        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
	        chooser.showDialog(new JLabel(), "选择");
	        File file = chooser.getSelectedFile();
	        textField.setText(file.getAbsoluteFile().toString());
	        path=textField.getText();
    	}else if(e.getActionCommand().equals("确定")){
    		int[][] data=new PicGenData().getData(path);
    		Recoginize rec = new Recoginize(data);
			char[] rec_result = rec.getChars_result();// 获得结果
			// 向show_recoginion中添加识别出的字符
			for (int i = 0; i < rec_result.length; i++) {
				rec_but[i].setText("" + rec_result[i]);
			}
    		this.dispose();
    	}
    }
}
