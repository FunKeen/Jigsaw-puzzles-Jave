package UI;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class GameFrame extends JFrame implements MouseListener {
    //记录鼠标点击、松开位置
    int mp;
    int np;
    int mr;
    int nr;
    //移动的步数
    int step;
    //拼图块大小
    int size;
    //创建初始拼图位置
    int[] tempArr;
    //创建4*4大小的拼图块
    int[][] arr = new int[4][4];
    //记录空白块的位置，默认3，3
    int emptyM;
    int emptyN;

    public void initData() {
        //初始化数值
        size = new ImageIcon("src/image/miku1/1.jpg").getIconWidth();
        tempArr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        step = 0;
        emptyM = 3;
        emptyN = 3;
    }

    //界面初始化函数（GameFrame的构造函数）
    public GameFrame() {
        //初始化窗口
        this.initGame();
        //初始化菜单栏
        this.initMenu();
        //初始化数据
        this.initData();
        //打乱拼图
        this.randomArray();
        //打印拼图
        this.initImage();

        this.setVisible(true);
    }

    //初始化游戏窗口
    private void initGame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("拼图游戏：初音");
        this.setSize(1097, 857);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.addMouseListener(this);
    }

    //初始化游戏菜单栏
    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu functionMenu = new JMenu("功能");
        JMenu aboutMenu = new JMenu("关于我");

        JMenuItem restart = new JMenuItem("重新开始");
        JMenuItem toWin = new JMenuItem("一键完成");
        JMenuItem exit = new JMenuItem("退出");

        JMenuItem account = new JMenuItem("微信号：Hao1919233");

        restart.addActionListener(e -> {
            initData();
            randomArray();
            initImage();
        });

        toWin.addActionListener(e -> {
            initData();
            initImage();
        });

        exit.addActionListener(e -> System.exit(0));

        functionMenu.add(restart);
        functionMenu.add(toWin);
        functionMenu.add(exit);

        aboutMenu.add(account);

        menuBar.add(functionMenu);
        menuBar.add(aboutMenu);

        this.setJMenuBar(menuBar);

    }

    //初始化拼图位置
    private void initImage() {
        this.getContentPane().removeAll();

        ImageIcon icon = new ImageIcon("src/image/miku1/all.jpg");
        JLabel label = new JLabel(icon);
        label.setBounds(23, 10, 256, 256);
        this.getContentPane().add(label);

        label = new JLabel("步数:" + step);
        label.setBounds(100, 300, 100, 30);
        this.getContentPane().add(label);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                ImageIcon icon1 = new ImageIcon("src/image/miku1/" + arr[i][j] + ".jpg");
                JLabel label1 = new JLabel(icon1);
                label1.setBounds(300 + j * size, 10 + i * size, size, size);
                label1.setBorder(new BevelBorder(BevelBorder.RAISED));
                this.getContentPane().add(label1);
            }
        }

        this.getContentPane().revalidate();
        this.getContentPane().repaint();
    }

    //随机打乱平涂块，并且使得拼图有解
    private void randomArray() {
        int[] tempArr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        for (int i = 0; i < 15; i++) {
            Random r = new Random();
            int x = r.nextInt(arr.length);
            int temp = tempArr[x];
            tempArr[x] = tempArr[i];
            tempArr[i] = temp;
        }
        int count = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = i + 1; j < 16; j++) {
                if (tempArr[i] > tempArr[j]) {
                    count++;
                }
            }
        }
        if (count % 2 == 0) {
            for (int i = 0; i < 4; i++) {
                System.arraycopy(tempArr, 4 * i, arr[i], 0, 4);
            }
        } else randomArray();
    }

    //判断鼠标松开位置是否在灰色方块中
    //用于优化操作
    private boolean isArea(int m, int n) {
        int m1 = 10 + emptyM * size;
        int n1 = 300 + emptyN * size;

        int m2 = m1 + size;
        int n2 = n1 + size;

        return m > m1 && m < m2 && n > n1 && n < n2;
    }

    //移动拼图块
    private void moveXY(int X, int Y) {
        if (Y >= 0 && Y <= 3 && X >= 0 && X <= 3) {
            arr[emptyM][emptyN] = arr[X][Y];
            arr[X][Y] = 16;
            emptyM = X;
            emptyN = Y;
            if (this.isWin()) {
                System.out.println("You are win!!!");
            } else {
                this.initImage();
            }
            step++;

        } else {
            System.out.println("无法移动！");
        }
    }

    //判断胜利条件
    private boolean isWin() {
        int num = 1;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (arr[i][j] != num) return false;
                num++;
            }
        }
        return true;
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        mp = e.getY();
        np = e.getX();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mr = e.getY();
        nr = e.getX();

        if (isArea(mr, nr)) {
            int flag = Math.abs(mp - mr) - Math.abs(np - nr);
            int X = 0;
            int Y = 0;

            if (flag < -20) {
                if (np < nr) {
                    Y = emptyN - 1;//右
                } else {
                    Y = emptyN + 1;//左
                }
                X = emptyM;
                moveXY(X, Y);
            } else if (flag > 20) {
                if (mp < mr) {
                    X = emptyM - 1;//下
                } else {
                    X = emptyM + 1;//上
                }
                Y = emptyN;
                moveXY(X, Y);
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}