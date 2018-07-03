package com.haxi.mh.other;

/**
 * 扫雷
 * Created by Han on 2018/6/28
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class Saolei2  {
//    Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize(); // 获得显示器大小对象
//
//    JFrame frame=new JFrame("扫雷游戏");//创建一个JFrame容器
//    JButton reset=new JButton("重来");//新建一个JButton组件
//    JButton exit=new JButton("退出");
//    JButton menu=new JButton("游戏介绍");
//    Container container=new Container();
//    JMenu menuFile = new JMenu("游戏菜单"),
//            menuEdit = new JMenu("重来"),
//            menuView = new JMenu("退出");
//    JMenuItem jmt1 = new JMenuItem("游戏介绍"),
//            jmt2 = new JMenuItem("重来"),
//            jmt3 = new JMenuItem("退出");
//
//
//    //游戏数据结构
//    final int row=20;//行数
//    final int col=20;//列数
//    final int leiCount=30;//有30颗雷
//    JButton [][] buttons=new JButton[row][col];
//    int [][] counts=new int[row][col];
//    final int LEICODE=10;   //这个leicode 表示个啥？
//
//    // 构造函数
//    public Saolei2(){
//        //1、设置窗口
//        frame.setSize(900, 800);//设置游戏的窗口大小
//        frame.setResizable(true);//是否可改变窗口大小
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//点击关闭按钮后退出游戏
//        frame.setLayout(new BorderLayout());//设置窗口布局
//        Dimension frameSize = frame.getSize();             // 获得窗口大小对象
//        frame.setLocation((displaySize.width - frameSize.width) / 2,
//                (displaySize.height - frameSize.height) / 2); //使游戏的窗口位于屏幕的中央
//
//        JMenuBar menuBar = new JMenuBar();
//        frame.setJMenuBar(menuBar);
//
//        //创建并添加各菜单，注意：菜单的快捷键是同时按下Alt键和字母键，方法setMnemonic('F')是设置快捷键为Alt +Ｆ
//
//        menuBar.add(menuFile);
//        // menuBar.add(menuEdit);
//        //menuBar.add(menuView);
//
//        menuFile.add(jmt1);
//        menuFile.add(jmt2);
//        menuFile.add(jmt3);
//        jmt1.addActionListener(new MenuAction1(this));
//        jmt2.addActionListener(new MenuAction1(this));
//        jmt3.addActionListener(new MenuAction1(this));
//        //2、添加重来按钮
////		addResetButton();
//        //2、添加退出按钮
//        //addExitButton();
//        //2、添加游戏介绍按钮
//        //addMenuButton();
//        //添加按钮
//        addButtons();
//
//        //埋雷
//        addLei();
//        //添加雷的计算
//        calcNeiboLei();
//        frame.setVisible(true);
//    }
//    public void addResetButton(){
//        reset.setBackground(Color.green);
//        //reset.setOpaque(true);
//        reset.addActionListener(this);
//        frame.add(reset,BorderLayout.NORTH);
//    }
//    public void addExitButton(){
//        exit.setBackground(Color.green);
//        //exit.setOpaque(true);
//        exit.addActionListener(this);
//        frame.add(exit,BorderLayout.EAST);
//    }
//    public void addMenuButton(){
//        menu.setBackground(Color.green);
//
//        //exit.setOpaque(true);
//        menu.addActionListener(this);
//        frame.add(menu,BorderLayout.SOUTH);
//    }
//
//    public void addLei(){
//        //这个函数是在随机分布雷。LEICODE 代表常量 ==LEICODE 说明在这个方块有雷，
//        //counts[randRow][randCol]说明 在ranRow行 randCol列 布置雷
//
//        Random rand=new Random();
//        int randRow,randCol;
//        for(int i=0;i<leiCount;i++){
//            randRow=rand.nextInt(row);
//            randCol=rand.nextInt(col);
//            if(counts[randRow][randCol]== LEICODE){
//                i--;
//            }else{
//                counts[randRow][randCol]=LEICODE;
//            }
//        }
//    }
//    public void addButtons(){
//        frame.add(container,BorderLayout.CENTER);
//        container.setLayout(new GridLayout(row,col));
//        for(int i=0;i<row;i++){
//            for(int j=0;j<col;j++){
//                JButton button=new JButton();
//                button.setBackground(Color.yellow);
//                button.setOpaque(true);
//                button.addActionListener(this);
//                buttons[i][j]=button;
//                container.add(button);
//            }
//        }
//    }
//
//
//    public void calcNeiboLei(){
//        int count;
//        for(int i=0;i<row;i++){
//            for(int j=0;j<col;j++){
//                count=0;
//                if(counts[i][j]==LEICODE) continue;
//
//                if(i>0 && j>0 && counts[i-1][j-1]==LEICODE) count++;
//                if(i>0&&counts[i-1][j]==LEICODE) count++;
//                if(i>0 && j<19 && counts[i-1][j+1]==LEICODE) count++;
//                if(j>0 && counts[i][j-1]==LEICODE) count++;
//                if(j<19 && counts[i][j+1]==LEICODE) count++;
//                if(i<19&&j>0&&counts[i+1][j-1]==LEICODE) count++;
//                if(i<19&&counts[i+1][j]==LEICODE) count++;
//                if(i<19&&j<19&&counts[i+1][j+1]==LEICODE) count++;
//
//                counts[i][j]=count;
//                //buttons[i][j].setText(counts[i][j]+"");
//            }
//        }
//    }
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        // TODO Auto-generated method stub
//        JButton button=(JButton)e.getSource();
//        String actionCommand = e.getActionCommand();
//        //下面的三个if分支，分别是点击游戏菜单、重来、退出、游戏介绍后的事件处理
//        if (actionCommand.equals(menuFile)) {
//            System.out.println("ssss");
//        }
//        if(button.equals(reset)){
//            for(int i=0;i<row;i++){
//                for(int j=0;j<col;j++){
//                    buttons[i][j].setText("");
//                    buttons[i][j].setEnabled(true);
//                    buttons[i][j].setBackground(Color.yellow);
//                    counts[i][j]=0;
//                }
//            }
//            addLei();
//            calcNeiboLei();
//        }else if(button.equals(exit)){
//            System.exit(0);
//        }else if(button.equals(menu)){
//
//            JFrame frame1=new JFrame("游戏介绍");
//            frame1.setSize(360, 320);
//            frame1.setVisible(true);
//
//            Dimension frameSize1 = frame1.getSize();             // 获得窗口大小对象
//            frame1.setLocation((displaySize.width - frameSize1.width) / 2,
//                    (displaySize.height - frameSize1.height) / 2);
//            JLabel jl = new JLabel("<html><body>&nbsp;&nbsp;&nbsp;&nbsp;游戏名称：扫雷<br><br>"
//                    + "&nbsp;&nbsp;&nbsp;游戏主区域由很多个方格组成。使用鼠标左键随机点击一个方格，方格即被打开并显示出方格中的数字；<br>"
//                    + "&nbsp;&nbsp;&nbsp;方格中数字则表示其周围的8个方格隐藏了几颗雷；如果点开的格子为空白格，即其周围有0颗雷，<br>"
//                    + "&nbsp;&nbsp;&nbsp;则其周围格子自动打开；如果其周围还有空白格，则会引发连锁反应；在你认为有雷的格子上，<br>"
//                    + "&nbsp;&nbsp;&nbsp;点击右键即可标记雷；如果一个已打开格子周围所有的雷已经正确标出，<br>"
//                    + "&nbsp;&nbsp;&nbsp;则可以在此格上同时点击鼠标左右键以打开其周围剩余的无雷格。</body></html>",JLabel.CENTER);
//            //在介绍游戏的窗口中用Label标签显示内容
//            frame1.add(jl, BorderLayout.CENTER);
//        }else{
//            int count=0;
//            for(int i=0;i<row;i++){
//                for(int j=0;j<col;j++){
//                    if(button.equals(buttons[i][j])){
//                        count=counts[i][j];
//                        if(count==LEICODE){
//                            LoseGame();
//                        }else{
//                            openCell(i,j);
//                            checkWin();
//                        } return;
//                    }
//                }
//            }
//
//
//        }
//    }
//
//
//
//
//
//    void checkWin(){
//        for(int i=0;i<row;i++){
//            for(int j=0;j<col;j++){
//                if(buttons[i][j].isEnabled()==true && counts[i][j]!=LEICODE) return;
//            }
//        }
//        JOptionPane.showMessageDialog(frame, "Yeah,你赢了！");
//    }
//    void openCell(int i,int j){
//        if(buttons[i][j].isEnabled()==false) return;
//
//        buttons[i][j].setEnabled(false);
//
//
//        if(counts[i][j]==0){
//            if(i>0 && j>0 && counts[i-1][j-1]!=LEICODE) openCell(i-1, j-1);
//            if(i>0&&counts[i-1][j]!=LEICODE) openCell(i-1, j);
//            if(i>0 && j<19 && counts[i-1][j+1]!=LEICODE) openCell(i-1, j+1);
//            if(j>0 && counts[i][j-1]!=LEICODE) openCell(i, j-1);
//            if(j<19 && counts[i][j+1]!=LEICODE) openCell(i, j+1);
//            if(i<19&&j>0&&counts[i+1][j-1]!=LEICODE) openCell(i+1, j-1);
//            if(i<19&&counts[i+1][j]!=LEICODE) openCell(i+1, j);
//            if(i<19&&j<19&&counts[i+1][j+1]!=LEICODE) openCell(i+1, j+1);
//
//            buttons[i][j].setText(counts[i][j]+"");
//        }else{
//            buttons[i][j].setText(counts[i][j]+"");
//        }
//    }
//    void LoseGame(){
//        for(int i=0;i<row;i++){
//            for(int j=0;j<col;j++){
//                int count=counts[i][j];
//                if(count==LEICODE){
//                    buttons[i][j].setText("X");
//                    buttons[i][j].setBackground(Color.red);
//                    buttons[i][j].setEnabled(false);
//                }else{
//                    buttons[i][j].setText(count+"");
//                    buttons[i][j].setEnabled(false);
//                }
//            }
//        }
//    }
//    public static void main(String[] args) {
//        Saolei2 lei=new Saolei2();
//    }
//
//
//
//}
//
//class MenuAction1 implements ActionListener {
//    Saolei2 m;
//    JFrame frame1=new JFrame("游戏介绍");
//    Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize(); // 获得显示器大小对象
//
//    MenuAction1(Saolei2 m) {
//        this.m = m;
//    }
//
//    public void actionPerformed(ActionEvent e) {
//        String color = e.getActionCommand();
//        if (color == "游戏介绍"){
//
//            frame1.setSize(360, 320);
//            frame1.setVisible(true);
//
//            Dimension frameSize1 = frame1.getSize();             // 获得窗口大小对象
//            frame1.setLocation((displaySize.width - frameSize1.width) / 2,
//                    (displaySize.height - frameSize1.height) / 2);
//            JLabel jl = new JLabel("<html><body>&nbsp;&nbsp;&nbsp;&nbsp;游戏名称：扫雷<br><br>"
//                    + "&nbsp;&nbsp;&nbsp;游戏主区域由很多个方格组成。使用鼠标左键随机点击一个方格，方格即被打开并显示出方格中的数字；<br>"
//                    + "&nbsp;&nbsp;&nbsp;方格中数字则表示其周围的8个方格隐藏了几颗雷；如果点开的格子为空白格，即其周围有0颗雷，<br>"
//                    + "&nbsp;&nbsp;&nbsp;则其周围格子自动打开；如果其周围还有空白格，则会引发连锁反应；在你认为有雷的格子上，<br>"
//                    + "&nbsp;&nbsp;&nbsp;点击右键即可标记雷；如果一个已打开格子周围所有的雷已经正确标出，<br>"
//                    + "&nbsp;&nbsp;&nbsp;则可以在此格上同时点击鼠标左右键以打开其周围剩余的无雷格。</body></html>",JLabel.CENTER);
//            frame1.add(jl, BorderLayout.CENTER);
//
//        }else if (color == "重来"){
//            for(int i=0;i<m.row;i++){
//                for(int j=0;j<m.col;j++){
//                    m.buttons[i][j].setText("");
//                    m.buttons[i][j].setEnabled(true);
//                    m.buttons[i][j].setBackground(Color.yellow);
//                    m.counts[i][j]=0;
//                }
//            }
//            m.addLei();
//            m.calcNeiboLei();
//
//        }else if (color == "退出")
//            System.exit(0);
//    }
}