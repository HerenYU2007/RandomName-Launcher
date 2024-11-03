import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class ProgramLauncher extends JFrame {

    private JButton launchGenshinButton;
    private JButton launchRandomNameButton;

    public ProgramLauncher() {
        super("随机点名启动器");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(470, 470);
        setLocationRelativeTo(null); // 居中显示
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(20, 20, 20, 20); // 设置边距

        // 设置背景图片
        ImageIcon backgroundIcon = new ImageIcon(getClass().getResource("/icon/NHFLS.png"));
        JLabel backgroundLabel = new JLabel(backgroundIcon);
        backgroundLabel.setHorizontalAlignment(JLabel.CENTER);
        backgroundLabel.setOpaque(false); // 设置透明
        backgroundLabel.setLayout(new GridBagLayout()); // 设置布局管理器

        // 设置按钮样式
        launchGenshinButton = new JButton("原神版随机点名！启动！");
        launchGenshinButton.setFont(new Font("宋体", Font.BOLD, 16)); // 设置字体大小
        launchGenshinButton.setMinimumSize(new Dimension(200, 50)); // 设置最小尺寸
        launchGenshinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                launchGenshinImpact();
                minimizeWindow(); // 启动程序后最小化窗口
            }
        });

        launchRandomNameButton = new JButton("随机点名！启动！");
        launchRandomNameButton.setFont(new Font("宋体", Font.BOLD, 16)); // 设置字体大小
        launchRandomNameButton.setMinimumSize(new Dimension(200, 50)); // 设置最小尺寸
        launchRandomNameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                launchRandomName();
                minimizeWindow(); // 启动程序后最小化窗口
            }
        });

        // 将按钮添加到背景标签上
        gbc.gridx = 0;
        gbc.gridy = 2; // 向上移动
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        backgroundLabel.add(launchGenshinButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4; // 向下移动
        backgroundLabel.add(launchRandomNameButton, gbc);

        contentPane.add(backgroundLabel, gbc);

        // 添加窗口关闭事件，确保程序完全停止
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0); // 完全退出程序
            }
        });
    }

    private void launchGenshinImpact() {
        String path = System.getProperty("user.dir") + File.separator + "Genshin" + File.separator + "genish-impact.exe";
        try {
            Process genshinProcess = Runtime.getRuntime().exec(path);
            monitorProcess(genshinProcess, true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to launch Genshin Impact: " + e.getMessage());
        }
    }

    private void launchRandomName() {
        String path = System.getProperty("user.dir") + File.separator + "randomname" + File.separator + "RandomName.jar";
        String javaCommand = "java -jar " + path;
        try {
            Process randomNameProcess = Runtime.getRuntime().exec(javaCommand);
            monitorProcess(randomNameProcess, false);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to launch RandomName: " + e.getMessage());
        }
    }

    private void monitorProcess(final Process process, final boolean isGenshin) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    int exitCode = process.waitFor();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            if (exitCode == 0) {
                                // 程序关闭后，恢复窗口
                                maximizeWindow();
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void minimizeWindow() {
        setState(JFrame.ICONIFIED); // 最小化窗口到任务栏
    }

    private void maximizeWindow() {
        setState(JFrame.NORMAL); // 恢复窗口
        toFront(); // 将窗口带到前台
        requestFocus(); // 请求焦点
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ProgramLauncher();
            }
        });
    }
}