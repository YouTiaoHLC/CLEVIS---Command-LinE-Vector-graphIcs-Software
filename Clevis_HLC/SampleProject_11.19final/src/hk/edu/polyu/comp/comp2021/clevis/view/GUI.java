package hk.edu.polyu.comp.comp2021.clevis.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import hk.edu.polyu.comp.comp2021.clevis.controller.Application;
import hk.edu.polyu.comp.comp2021.clevis.model.CLI;
import hk.edu.polyu.comp.comp2021.clevis.model.shapes.*;
import hk.edu.polyu.comp.comp2021.clevis.model.shapes.Rectangle;
import hk.edu.polyu.comp.comp2021.clevis.model.shapes.Shape;

/**
 * This class is done by my groupmate, which creates the UI of this clevis, making the shapes stored visible.
 */
public class GUI extends JFrame {
    private DrawingPanel drawingPanel;
    private JTextField commandInput;
    private JTextArea outputArea;
    private Application app;
    private JLabel statusLabel;
    private JList<String> historyList;
    private DefaultListModel<String> historyModel;

    public GUI(Application app) {
        this.app = app;
        app.setGUI(this);
        initGUI();
        showWelcomeMessage();
        System.out.println("CLEVIS TOOL IS READY!");
    }

    private void initGUI() {
        setTitle("Clevis - Graphical Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800); // 增加窗口大小以容纳所有组件
        setLocationRelativeTo(null);

        // 使用 BorderLayout
        setLayout(new BorderLayout(5, 5));

        createToolbar();
        createMainContent(); // 只调用这个，它会创建图形区域和历史区域
        createBottomPanel();
    }

    private void createMainContent() {
        // 使用 JSplitPane 来分割图形区域和历史区域
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(800);
        splitPane.setResizeWeight(0.7);

        splitPane.setLeftComponent(createDrawingArea());
        splitPane.setRightComponent(createHistoryPanel());

        add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createDrawingArea() {
        JPanel drawingContainer = new JPanel(new BorderLayout());
        drawingContainer.setBorder(BorderFactory.createTitledBorder(
                "Graphics Display - Drag to pan, Ctrl+Mouse Wheel to zoom"));

        drawingPanel = new DrawingPanel();
        drawingPanel.setBackground(Color.WHITE);

        JScrollPane drawingScrollPane = new JScrollPane(drawingPanel);
        drawingScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        drawingScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        drawingContainer.add(drawingScrollPane, BorderLayout.CENTER);
        return drawingContainer;
    }

    // 在 GUI.java 的 createHistoryPanel() 方法中，移除按钮面板部分
    private JPanel createHistoryPanel() {
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createTitledBorder("Command History"));
        historyPanel.setPreferredSize(new Dimension(300, 0));

        // 创建历史列表
        historyModel = new DefaultListModel<>();
        historyList = new JList<>(historyModel);
        historyList.setFont(new Font("Consolas", Font.PLAIN, 11));
        historyList.setBackground(new Color(250, 250, 250));

        JScrollPane historyScrollPane = new JScrollPane(historyList);
        historyScrollPane.setPreferredSize(new Dimension(280, 0));

        // 移除按钮面板，只保留历史列表
        historyPanel.add(historyScrollPane, BorderLayout.CENTER);

        return historyPanel;
    }

    private void clearHistory() {
        historyModel.clear();
        appendOutput("Command history cleared");
    }

    private void rerunSelectedCommand() {
        String selectedCommand = historyList.getSelectedValue();
        if (selectedCommand != null) {
            commandInput.setText(selectedCommand);
            appendOutput("Command ready for rerun: " + selectedCommand);
            setStatus("Selected command loaded into input field");
        } else {
            appendOutput("No command selected from history");
            setStatus("Please select a command from history first");
        }
    }

    // 添加这个方法：将命令添加到历史列表
    public void addToHistory(String command) {
        SwingUtilities.invokeLater(() -> {
            historyModel.addElement(command);
            // 自动滚动到最后
            int lastIndex = historyModel.size() - 1;
            if (lastIndex >= 0) {
                historyList.ensureIndexIsVisible(lastIndex);
            }
        });
    }

    private void createToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setPreferredSize(new Dimension(1000, 40));

        JButton zoomInBtn = createToolbarButton("Zoom In", "+");
        JButton zoomOutBtn = createToolbarButton("Zoom Out", "-");
        JButton resetZoomBtn = createToolbarButton("Reset Zoom", "Reset");
        JButton refreshBtn = createToolbarButton("Refresh", "Refresh");
        JButton clearGraphicsBtn = createToolbarButton("Clear Graphics", "Clear");
        JButton usecmd = createToolbarButton("Use Command Line", "UseCMD");

        zoomInBtn.addActionListener(e -> drawingPanel.zoomIn());
        zoomOutBtn.addActionListener(e -> drawingPanel.zoomOut());
        resetZoomBtn.addActionListener(e -> drawingPanel.resetZoom());
        refreshBtn.addActionListener(e -> refreshGraphics());
        clearGraphicsBtn.addActionListener(e -> clearGraphics());
        usecmd.addActionListener(e -> useCMD());

        toolBar.add(zoomInBtn);
        toolBar.add(zoomOutBtn);
        toolBar.add(resetZoomBtn);
        toolBar.add(refreshBtn);
        toolBar.addSeparator();
        toolBar.add(clearGraphicsBtn);
        toolBar.add(usecmd);

        add(toolBar, BorderLayout.NORTH);
    }

    private JButton createToolbarButton(String tooltip, String text) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(80, 30));
        return button;
    }

    private void clearGraphics() {
        // 清除所有图形
        CLI.getShapes_map().clear();
        CLI.getShapes_list().clear();
        refreshGraphics();
        appendOutput("All graphics cleared");
        setStatus("Graphics cleared");
    }
    private void useCMD() {
        this.dispose();
        app.runCLI();
    }

    private void createBottomPanel() {
        // 创建底部主面板
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setPreferredSize(new Dimension(800, 250));

        // 创建CLI区域和状态栏
        JPanel cliPanel = createCLIArea();
        JPanel statusPanel = createStatusBar();

        // 将CLI区域放在中间，状态栏放在南部
        bottomPanel.add(cliPanel, BorderLayout.CENTER);
        bottomPanel.add(statusPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createCLIArea() {
        JPanel cliPanel = new JPanel(new BorderLayout());
        cliPanel.setBorder(BorderFactory.createTitledBorder("Clevis Terminal"));
        cliPanel.setPreferredSize(new Dimension(800, 200));

        // 使用现代深色主题
        Color terminalBg = new Color(30, 30, 30);
        Color terminalText = new Color(220, 220, 220);

        outputArea = new JTextArea(10, 60);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        outputArea.setBackground(terminalBg);
        outputArea.setForeground(terminalText);
        outputArea.setCaretColor(Color.WHITE);
        outputArea.setMargin(new Insets(5, 5, 5, 5));

        JScrollPane outputScrollPane = new JScrollPane(outputArea);

        // 输入区域
        JPanel inputPanel = new JPanel(new BorderLayout(5, 0));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        inputPanel.setBackground(terminalBg);

        JLabel promptLabel = new JLabel("$ ");
        promptLabel.setFont(new Font("Consolas", Font.BOLD, 13));
        promptLabel.setForeground(new Color(0, 200, 0));
        promptLabel.setBackground(terminalBg);
        promptLabel.setOpaque(true);

        commandInput = new JTextField();
        commandInput.setFont(new Font("Consolas", Font.PLAIN, 13));
        commandInput.setBackground(terminalBg);
        commandInput.setForeground(terminalText);
        commandInput.setCaretColor(Color.WHITE);
        commandInput.setBorder(null);
        commandInput.addActionListener(new CommandListener());

        inputPanel.add(promptLabel, BorderLayout.WEST);
        inputPanel.add(commandInput, BorderLayout.CENTER);

        cliPanel.add(outputScrollPane, BorderLayout.CENTER);
        cliPanel.add(inputPanel, BorderLayout.SOUTH);

        return cliPanel;
    }

    private JPanel createStatusBar() {
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        statusPanel.setPreferredSize(new Dimension(800, 25));

        statusLabel = new JLabel("Ready - Enter commands in the input field below");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(Color.DARK_GRAY);

        JLabel scaleLabel = new JLabel("Scale: 1.0x");
        scaleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        scaleLabel.setForeground(Color.GRAY);

        if (drawingPanel != null) {
            drawingPanel.addScaleChangeListener(scale -> {
                scaleLabel.setText(String.format("Scale: %.1fx", scale));
            });
        }

        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(scaleLabel, BorderLayout.EAST);

        return statusPanel;
    }

    public void appendOutput(String text) {
        SwingUtilities.invokeLater(() -> {
            outputArea.append(text + "\n");
            outputArea.setCaretPosition(outputArea.getDocument().getLength());
        });
    }

    public void setStatus(String status) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText(status);
        });
    }

    public void refreshGraphics() {
        SwingUtilities.invokeLater(() -> {
            drawingPanel.repaint();
            setStatus("Graphics refreshed - Shapes: " + CLI.getShapes_list().size());
        });
    }

    private class CommandListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = commandInput.getText().trim();
            if (!command.isEmpty()) {
                appendOutput("clevis> " + command);
                addToHistory(command); // 添加到历史列表
                processCommand(command);
                commandInput.setText("");
                setStatus("Command executed: " + command);
            }
        }
    }

    private void processCommand(String command) {
        try {
            boolean success = app.processCommand(command);
            if (success) {
                setStatus("Command executed successfully");
                refreshGraphics();
            } else {
                setStatus("Command failed");
            }
        } catch (Exception ex) {
            appendOutput("Error: " + ex.getMessage());
            setStatus("Command failed: " + ex.getMessage());
        }
    }

    private void showWelcomeMessage() {
        SwingUtilities.invokeLater(() -> {
            appendOutput("======= Clevis Graphical Editor =======");
            appendOutput("CLEVIS TOOL IS READY!");
            appendOutput("Please enter commands in the input field below");
            appendOutput("");
            appendOutput("Available commands:");
            appendOutput("  rectangle <name> <x> <y> <width> <height>");
            appendOutput("  circle <name> <x> <y> <radius>");
            appendOutput("  line <name> <x1> <y1> <x2> <y2>");
            appendOutput("  square <name> <x> <y> <side>");
            appendOutput("  group <groupName> <shape1> <shape2> ...");
            appendOutput("  move <shapeName> <dx> <dy>");
            appendOutput("  delete <shapeName>");
            appendOutput("  listall");
            appendOutput("  help");
            appendOutput("  quit");
            appendOutput("");
            appendOutput("Then press Enter or click Execute");
            appendOutput("");
        });
    }

    class DrawingPanel extends JPanel {
        private double scale = 1.0;
        private static final double SCALE_FACTOR = 1.2;
        private static final double MIN_SCALE = 0.1;
        private static final double MAX_SCALE = 10.0;

        private int panX = 0;
        private int panY = 0;
        private Point dragStart;

        private List<ScaleChangeListener> scaleListeners = new ArrayList<>();

        public DrawingPanel() {
            enableDragPanning();
            enableMouseWheelZoom();
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            g2d.translate(centerX, centerY);
            g2d.translate(panX, panY);
            g2d.scale(scale, scale);
            drawBackground(g2d);
            drawAllShapes(g2d);
            drawCoordinates(g2d);
        }

        private void drawBackground(Graphics2D g2d) {
            int width = getWidth();
            int height = getHeight();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(-width/2, -height/2, width, height);
            drawGrid(g2d, width, height);
        }

        private void drawGrid(Graphics2D g2d, int width, int height) {
            int gridSize = (int) (50 / scale);
            if (gridSize < 5) gridSize = 5;

            g2d.setColor(new Color(240, 240, 240));
            int halfWidth = width / 2;
            int halfHeight = height / 2;

            // 垂直线（以Y轴为中心）
            for (int x = -halfWidth; x <= halfWidth; x += gridSize) {
                g2d.drawLine(x, -halfHeight, x, halfHeight);
            }
            // 水平线（以X轴为中心）
            for (int y = -halfHeight; y <= halfHeight; y += gridSize) {
                g2d.drawLine(-halfWidth, y, halfWidth, y);
            }

            // 绘制坐标轴（在原点相交）
            g2d.setColor(new Color(200, 200, 200));
            g2d.setStroke(new BasicStroke(2)); // 粗一点的轴线

            // X轴（水平线，在y=0处）
            g2d.drawLine(-halfWidth, 0, halfWidth, 0);
            // Y轴（垂直线，在x=0处）
            g2d.drawLine(0, -halfHeight, 0, halfHeight);

            g2d.setStroke(new BasicStroke(1)); // 恢复默认线条粗细

            // 绘制坐标轴箭头和刻度
            drawAxisArrows(g2d, halfWidth, halfHeight);
            drawAxisTicks(g2d, halfWidth, halfHeight);
        }

        private void drawAxisArrows(Graphics2D g2d, int halfWidth, int halfHeight) {
            int arrowSize = 8;

            // X轴箭头（向右）
            g2d.setColor(Color.RED);
            g2d.fillPolygon(new int[]{halfWidth, halfWidth - arrowSize, halfWidth - arrowSize},
                    new int[]{0, -arrowSize/2, arrowSize/2}, 3);

            // Y轴箭头（向上）
            g2d.setColor(Color.BLUE);
            g2d.fillPolygon(new int[]{0, -arrowSize/2, arrowSize/2},
                    new int[]{-halfHeight, -halfHeight + arrowSize, -halfHeight + arrowSize}, 3);

            // 坐标轴标签
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString("X", halfWidth - 20, 15);
            g2d.drawString("Y", -20, -halfHeight + 20);
        }

        private void drawAxisTicks(Graphics2D g2d, int halfWidth, int halfHeight) {
            int tickSize = 5;
            int labelSpacing = 50; // 标签间距（像素）

            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));

            // X轴刻度（水平轴）
            for (int x = -halfWidth + labelSpacing; x <= halfWidth - labelSpacing; x += labelSpacing) {
                if (x == 0) continue; // 跳过原点

                // 绘制刻度线
                g2d.drawLine(x, -tickSize, x, tickSize);

                // 绘制刻度值
                String label = String.valueOf(x);
                FontMetrics fm = g2d.getFontMetrics();
                int labelWidth = fm.stringWidth(label);
                g2d.drawString(label, x - labelWidth/2, 15);
            }

            // Y轴刻度（垂直轴）
            for (int y = -halfHeight + labelSpacing; y <= halfHeight - labelSpacing; y += labelSpacing) {
                if (y == 0) continue; // 跳过原点

                // 绘制刻度线
                g2d.drawLine(-tickSize, y, tickSize, y);

                // 绘制刻度值（注意Y坐标需要翻转显示）
                String label = String.valueOf(-y); // 翻转Y值显示
                FontMetrics fm = g2d.getFontMetrics();
                int labelHeight = fm.getHeight();
                g2d.drawString(label, -20, y + labelHeight/3);
            }

            // 原点标记
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Arial", Font.BOLD, 11));
            g2d.drawString("O(0,0)", 5, 15);
        }

        private void drawAllShapes(Graphics2D g2d) {
            // 从 CLI 获取所有图形并绘制
            List<Shape> shapes = CLI.getShapes_list();
            if (shapes != null && !shapes.isEmpty()) {
                for (Shape shape : shapes) {
                    drawShape(g2d, shape);
                }
            }
        }

        private void drawShape(Graphics2D g2d, Shape shape) {
            if (shape instanceof Rectangle) {
                drawRectangle(g2d, (hk.edu.polyu.comp.comp2021.clevis.model.shapes.Rectangle) shape);
            } else if (shape instanceof Circle) {
                drawCircle(g2d, (Circle) shape);
            } else if (shape instanceof Line) {
                drawLine(g2d, (Line) shape);
            } else if (shape instanceof Square) {
                drawSquare(g2d, (Square) shape);
            }
        }

        // 坐标转换方法
        private int toGraphicsX(double mathX) {
            return (int) mathX; // X坐标不变
        }

        private int toGraphicsY(double mathY) {
            return (int) -mathY; // Y坐标翻转（数学坐标系Y向上，图形坐标系Y向下）
        }

        private void drawRectangle(Graphics2D g2d, hk.edu.polyu.comp.comp2021.clevis.model.shapes.Rectangle rect) {
            int x = toGraphicsX(rect.getX());
            int y = toGraphicsY(rect.getY());
            int width = (int) rect.getW();
            int height = (int) rect.getH();



            g2d.setColor(new Color(70, 130, 180, 200));
            g2d.fillRect(x, y , width, height);
            g2d.setColor(Color.BLUE);
            g2d.drawRect(x, y , width, height);

            // 绘制名称标签
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString(rect.getName(), x, y - height - 5);
        }

        private void drawCircle(Graphics2D g2d, Circle circle) {
            int radius = (int) circle.getRadius();
            int x = toGraphicsX(circle.getX());
            int y = toGraphicsY(circle.getY());

            g2d.setColor(new Color(220, 20, 60, 150));
            g2d.fillOval(x - radius, y-radius, radius * 2, radius * 2);
            g2d.setColor(Color.RED);
            g2d.drawOval(x - radius, y-radius, radius * 2, radius * 2);

            // 绘制名称标签 - 显示在圆心位置
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));

            // 计算文本宽度以居中显示
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(circle.getName());
            int textHeight = fm.getHeight();

            // 在圆心位置显示名称
            g2d.drawString(circle.getName(), x - textWidth/2, y + textHeight/3);
        }

        private void drawLine(Graphics2D g2d, Line line) {
            int x1 = toGraphicsX(line.getX1());
            int y1 = toGraphicsY(line.getY1());
            int x2 = toGraphicsX(line.getX2());
            int y2 = toGraphicsY(line.getY2());


            g2d.setColor(Color.GREEN);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(x1, y1, x2, y2);
            g2d.setStroke(new BasicStroke(1));

            // 绘制名称标签（在线段中点）
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            int midX = (x1 + x2) / 2;
            int midY = (y1 + y2) / 2;
            g2d.drawString(line.getName(), midX, midY - 5);
        }

        private void drawSquare(Graphics2D g2d, Square square) {
            int side = (int) square.getL();
            int x = toGraphicsX(square.getX());
            int y = toGraphicsY(square.getY());



            g2d.setColor(new Color(255, 165, 0, 200));
            g2d.fillRect(x, y, side, side);
            g2d.setColor(Color.ORANGE);
            g2d.drawRect(x, y, side, side);

            // 绘制名称标签
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));
            g2d.drawString(square.getName(), x, y - side - 5);
        }

        private void drawCoordinates(Graphics2D g2d) {
            g2d.setColor(Color.GRAY);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            // 显示在左上角（相对于画布，不是相对于坐标原点）
            g2d.drawString(String.format("Scale: %.1fx | Pan: (%d, %d)", scale, panX, panY), -getWidth()/2 + 10, -getHeight()/2 + 15);
        }

        private void enableDragPanning() {
            addMouseListener(new MyMouseAdapter());

            addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                @Override
                public void mouseDragged(java.awt.event.MouseEvent e) {
                    if (dragStart != null) {
                        Point dragEnd = e.getPoint();
                        panX += dragEnd.x - dragStart.x;
                        panY += dragEnd.y - dragStart.y;
                        dragStart = dragEnd;
                        repaint();
                    }
                }
            });
        }

        private void enableMouseWheelZoom() {
            addMouseWheelListener(e -> {
                if (e.isControlDown()) {
                    double oldScale = scale;
                    if (e.getWheelRotation() < 0) {
                        zoomIn();
                    } else {
                        zoomOut();
                    }
                    if (oldScale != scale) {
                        fireScaleChanged();
                    }
                }
            });
        }

        public void zoomIn() {
            if (scale * SCALE_FACTOR <= MAX_SCALE) {
                scale *= SCALE_FACTOR;
                repaint();
                fireScaleChanged();
            }
        }

        public void zoomOut() {
            if (scale / SCALE_FACTOR >= MIN_SCALE) {
                scale /= SCALE_FACTOR;
                repaint();
                fireScaleChanged();
            }
        }

        public void resetZoom() {
            scale = 1.0;
            panX = 0;
            panY = 0;
            repaint();
            fireScaleChanged();
        }

        public void addScaleChangeListener(ScaleChangeListener listener) {
            scaleListeners.add(listener);
        }

        private void fireScaleChanged() {
            for (ScaleChangeListener listener : scaleListeners) {
                listener.onScaleChanged(scale);
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(800, 400);
        }

        private class MyMouseAdapter extends java.awt.event.MouseAdapter {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                dragStart = e.getPoint();
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
//                    dragStart = null;
                setCursor(Cursor.getDefaultCursor());
            }
        }
    }

    interface ScaleChangeListener {
        void onScaleChanged(double scale);
    }

    public static void startGUI(Application app) {
        // 设置现代外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI(app);
            gui.setVisible(true);
        });
    }
}