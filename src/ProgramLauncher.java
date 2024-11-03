import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class ProgramLauncher extends JFrame {

    private JButton launchGenshinButton;
    private JButton launchRandomNameButton;

    public ProgramLauncher() {
        super("Program Launcher");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // 居中显示
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // 设置边距

        launchGenshinButton = new JButton("Launch Genshin Impact");
        launchGenshinButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                launchGenshinImpact();
                minimizeWindow(); // 启动程序后最小化窗口
            }
        });
        gbc.anchor = GridBagConstraints.CENTER;
        contentPane.add(launchGenshinButton, gbc);

        launchRandomNameButton = new JButton("Launch RandomName");
        launchRandomNameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                launchRandomName();
                minimizeWindow(); // 启动程序后最小化窗口
            }
        });
        contentPane.add(launchRandomNameButton, gbc);

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