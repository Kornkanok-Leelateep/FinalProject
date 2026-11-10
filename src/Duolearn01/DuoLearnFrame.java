package Duolearn01;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DuoLearnFrame extends JFrame {

    private final AppData data;
    private final LearningService service;

    private JLabel xpValue;
    private JLabel accuracyValue;
    private JLabel lessonValue;

    private JLabel quizWord;
    private JLabel quizFeedback;
    private JTextField answerField;
    private JButton checkButton;
    private JButton nextButton;

    private String selectedCategory;
    private JTabbedPane tabs;
    private boolean answered = false;

    // สีตัวอักษรของปุ่ม
    private static final Color BUTTON_TEXT =
            new Color(40, 55, 65);

    // ฟอนต์หลัก
    private static final Font BUTTON_FONT =
            new Font("Tahoma", Font.BOLD, 15);

    private Font thaiFont(int size, int style) {
        return new Font("Leelawadee UI", style, size);
    }

    public DuoLearnFrame() {

        data = DataStore.load();
        service = new LearningService(data);

        setTitle("DuoLearn | Learn a little every day");
        java.net.URL iconURL = getClass().getResource("icon.png");

        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            setIconImage(icon.getImage());
        } else {
            System.out.println("ไม่พบไฟล์ icon.png");
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 760);
        setMinimumSize(new Dimension(900, 650));
        setLocationRelativeTo(null);

        getContentPane().setBackground(UITheme.CREAM);
        setLayout(new BorderLayout());

        // ตั้งค่าฟอนต์ภาษาไทยสำหรับหน้าต่างแจ้งเตือน
        UIManager.put(
                "OptionPane.messageFont",
                new Font("Tahoma", Font.PLAIN, 14)
        );

        UIManager.put(
                "OptionPane.buttonFont",
                new Font("Tahoma", Font.BOLD, 14)
        );

        add(createHeader(), BorderLayout.NORTH);
        add(createTabs(), BorderLayout.CENTER);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                DataStore.save(data);
            }
        });

        refreshStats();
    }

    // ================= HEADER =================


    private JPanel createHeader() {

        JPanel header = new JPanel(new BorderLayout(18, 0));
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(18, 28, 18, 28));

        JLabel logo = new JLabel();

        java.net.URL logoURL = getClass().getResource("logo.png");

        if (logoURL != null) {
            ImageIcon originalIcon = new ImageIcon(logoURL);
            Image image = originalIcon.getImage().getScaledInstance(58, 58, Image.SCALE_SMOOTH);
            logo.setIcon(new ImageIcon(image));
        } else {
            logo.setText("🌱");
            System.out.println("ไม่พบไฟล์ logo.png");
        }

        JLabel brand = new JLabel("DuoLearn");
        brand.setFont(new Font("Tahoma", Font.BOLD, 28));
        brand.setForeground(UITheme.DARK_GREEN);

        JLabel tagline = new JLabel("เรียนวันละนิด เก่งภาษาได้ทุกวัน");
        tagline.setFont(thaiFont(14, Font.PLAIN));
        tagline.setForeground(UITheme.MUTED);

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 3));
        textPanel.setOpaque(false);
        textPanel.add(brand);
        textPanel.add(tagline);

        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        brandPanel.setOpaque(false);
        brandPanel.add(logo);
        brandPanel.add(textPanel);

        JLabel xp = new JLabel(data.getXp() + " XP");
        xp.setFont(new Font("Tahoma", Font.BOLD, 18));
        xp.setForeground(new Color(220, 145, 20));
        xp.setOpaque(true);
        xp.setBackground(new Color(255, 247, 220));
        xp.setBorder(new EmptyBorder(10, 18, 10, 18));

        header.add(brandPanel, BorderLayout.WEST);
        header.add(xp, BorderLayout.EAST);
        return header;
    }

    // ================= TABS =================

    private JTabbedPane createTabs() {

        tabs = new JTabbedPane();

        tabs.setFont(thaiFont(15, Font.BOLD));
        tabs.setBackground(Color.WHITE);
        tabs.setForeground(UITheme.INK);
        tabs.setBorder(BorderFactory.createEmptyBorder(4, 8, 0, 8));
        tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        UIManager.put("TabbedPane.selected", UITheme.PALE_GREEN);
        UIManager.put("TabbedPane.contentAreaColor", UITheme.CREAM);
        UIManager.put("TabbedPane.focus", UITheme.CREAM);

        tabs.addTab("หน้าหลัก", createDashboard());
        tabs.addTab("บทเรียน", createLessons());
        tabs.addTab("แบบทดสอบ", createQuiz());
        tabs.addTab("คลังคำศัพท์", createVocabulary());
        tabs.addTab("ทบทวน", createReview());
        tabs.addTab("ประวัติ", createHistory());

        return tabs;
    }

    // ================= PAGE =================

    private JPanel page() {

        JPanel panel = new JPanel(
                new BorderLayout(14, 14)
        );

        panel.setBackground(UITheme.CREAM);

        panel.setBorder(
                new EmptyBorder(26, 30, 28, 30)
        );

        return panel;
    }

    // ================= BUTTON STYLE =================

    private void styleDarkTextButton(
            JButton button,
            Color background
    ) {

        UITheme.styleButton(button, background);

        button.setFont(BUTTON_FONT);
        button.setForeground(BUTTON_TEXT);

        button.setEnabled(true);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setFocusPainted(false);
    }

    // ================= DASHBOARD =================

    private JPanel createDashboard() {

        JPanel panel = page();

        JPanel top = new JPanel(
                new GridLayout(1, 3, 14, 14)
        );

        top.setOpaque(false);

        xpValue = statCard(
                "คะแนนสะสม",
                data.getXp() + " XP",
                UITheme.ORANGE
        );

        accuracyValue = statCard(
                "ตอบถูก",
                accuracy() + "%",
                UITheme.GREEN
        );

        lessonValue = statCard(
                "บทเรียนที่จบ",
                String.valueOf(data.getLessonsCompleted()),
                UITheme.BLUE
        );

        top.add(wrapStat(
                "คะแนนสะสม", xpValue, UITheme.ORANGE
        ));

        top.add(wrapStat(
                "ความแม่นยำ", accuracyValue, UITheme.GREEN
        ));

        top.add(wrapStat(
                "บทเรียนที่จบ", lessonValue, UITheme.BLUE
        ));

        JPanel welcome = UITheme.card();

        welcome.setLayout(
                new BoxLayout(welcome, BoxLayout.Y_AXIS)
        );

        JLabel title = UITheme.heading(
                "ยินดีต้อนรับสู่ DuoLearn!", 25
        );
        title.setFont(thaiFont(25, Font.BOLD));

        JLabel desc = new JLabel(
                "เลือกบทเรียนสั้น ๆ แล้วเริ่มฝึกภาษาอังกฤษกันเลย"
        );

        desc.setFont(thaiFont(16, Font.PLAIN));

        desc.setForeground(UITheme.MUTED);

        // ปุ่มไปที่บทเรียน
        JButton go = new JButton("ไปที่บทเรียน");

        styleDarkTextButton(go, UITheme.GREEN);

        go.setFont(
                new Font("Tahoma", Font.BOLD, 17)
        );

        go.addActionListener(e ->
                tabs.setSelectedIndex(1)
        );

        welcome.setBorder(new javax.swing.border.CompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 238, 222), 1, true),
                new EmptyBorder(30, 32, 30, 32)
        ));
        welcome.add(Box.createVerticalGlue());
        welcome.add(title);
        welcome.add(Box.createVerticalStrut(14));
        welcome.add(desc);
        welcome.add(Box.createVerticalStrut(24));
        go.setAlignmentX(Component.LEFT_ALIGNMENT);
        go.setPreferredSize(new Dimension(190, 48));
        go.setMaximumSize(new Dimension(220, 48));
        welcome.add(go);
        welcome.add(Box.createVerticalGlue());

        panel.add(top, BorderLayout.NORTH);
        panel.add(welcome, BorderLayout.CENTER);

        return panel;
    }

    // ================= STAT CARD =================

    private JLabel statCard(
            String title,
            String value,
            Color color
    ) {

        JLabel label = new JLabel(value);

        label.setFont(
                new Font("Tahoma", Font.BOLD, 25)
        );

        label.setForeground(color);

        return label;
    }

    private JPanel wrapStat(
            String title,
            JLabel value,
            Color color
    ) {

        JPanel card = UITheme.card();

        card.setLayout(
                new BoxLayout(card, BoxLayout.Y_AXIS)
        );
        card.setBorder(new javax.swing.border.CompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 235, 228), 1, true),
                new EmptyBorder(18, 20, 18, 20)
        ));

        JLabel name = new JLabel(title);

        name.setFont(thaiFont(15, Font.BOLD));

        name.setForeground(UITheme.MUTED);

        card.add(name);
        card.add(Box.createVerticalStrut(10));
        card.add(value);

        return card;
    }

    // ================= LESSONS =================


    private JPanel createLessons() {

        JPanel panel = page();

        JLabel title = UITheme.heading(
                "เลือกหมวดหมู่บทเรียน", 23
        );

        title.setFont(thaiFont(23, Font.BOLD));

        JPanel categoryPanel = new JPanel(
                new GridLayout(0, 2, 18, 18)
        );

        categoryPanel.setOpaque(false);
        categoryPanel.setBorder(
                new EmptyBorder(15, 0, 15, 0)
        );

        for (String category : service.getCategories()) {

            int wordCount =
                    service.getWordsInCategory(category).size();

            JButton button = new JButton();

            button.setLayout(new BorderLayout());
            button.setBackground(new Color(226, 247, 225));
            button.setFocusPainted(false);
            button.setCursor(
                    new Cursor(Cursor.HAND_CURSOR)
            );

            button.setBorder(new javax.swing.border.CompoundBorder(
                    BorderFactory.createLineBorder(new Color(198, 228, 199), 1, true),
                    new EmptyBorder(12, 16, 12, 16)
            ));

            button.setPreferredSize(
                    new Dimension(250, 145)
            );

            // Panel สำหรับข้อความในการ์ด
            JPanel content = new JPanel();

            content.setLayout(
                    new BoxLayout(content, BoxLayout.Y_AXIS)
            );

            content.setOpaque(false);

            // ชื่อหมวดหมู่
            JLabel categoryName = new JLabel(category);

            categoryName.setFont(
                    thaiFont(22, Font.BOLD)
            );

            categoryName.setForeground(UITheme.DARK_GREEN);

            categoryName.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            categoryName.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            // จำนวนคำศัพท์
            JLabel wordLabel = new JLabel(
                    wordCount + " คำศัพท์"
            );

            wordLabel.setFont(
                    thaiFont(15, Font.PLAIN)
            );

            wordLabel.setForeground(
                    new Color(55, 75, 65)
            );

            wordLabel.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            content.add(Box.createVerticalGlue());
            content.add(categoryName);
            content.add(Box.createVerticalStrut(10));
            content.add(wordLabel);
            content.add(Box.createVerticalGlue());

            button.add(content, BorderLayout.CENTER);

            // เมื่อกดเลือกหมวดหมู่
            button.addActionListener(e -> {

                selectedCategory = category;

                service.startLesson(category);

                JOptionPane.showMessageDialog(
                        this,
                        "เลือกหมวด " + category +
                                " แล้ว\nไปที่แท็บแบบทดสอบเพื่อเริ่มทำได้เลย",
                        "DuoLearn",
                        JOptionPane.INFORMATION_MESSAGE
                );
            });

            categoryPanel.add(button);
        }

        panel.add(title, BorderLayout.NORTH);
        panel.add(categoryPanel, BorderLayout.CENTER);

        return panel;
    }


    // ================= QUIZ =================

    private JPanel createQuiz() {

        JPanel panel = page();

        JPanel quizCard = UITheme.card();

        quizCard.setLayout(
                new BoxLayout(quizCard, BoxLayout.Y_AXIS)
        );
        quizCard.setBorder(new javax.swing.border.CompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 238, 222), 1, true),
                new EmptyBorder(28, 34, 30, 34)
        ));

        JLabel title = UITheme.heading(
                "ฝึกคำศัพท์", 23
        );

        title.setFont(thaiFont(23, Font.BOLD));

        quizWord = new JLabel(
                "เลือกบทเรียนก่อน แล้วกดเริ่มทำแบบทดสอบ",
                SwingConstants.CENTER
        );

        quizWord.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        quizWord.setFont(
                thaiFont(25, Font.BOLD)
        );

        quizWord.setForeground(UITheme.INK);

        answerField = new JTextField();

        answerField.setMaximumSize(
                new Dimension(480, 50)
        );
        answerField.setPreferredSize(new Dimension(480, 50));
        answerField.setBorder(new javax.swing.border.CompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 215, 196), 1, true),
                new EmptyBorder(8, 14, 8, 14)
        ));

        answerField.setFont(
                thaiFont(18, Font.PLAIN)
        );

        answerField.setHorizontalAlignment(
                JTextField.CENTER
        );

        quizFeedback = new JLabel(
                "พิมพ์คำแปลภาษาไทย",
                SwingConstants.CENTER
        );

        quizFeedback.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        quizFeedback.setFont(
                thaiFont(16, Font.BOLD)
        );

        quizFeedback.setForeground(UITheme.MUTED);

        JButton start = new JButton("เริ่ม / เริ่มใหม่");

        checkButton = new JButton("ตรวจคำตอบ");

        nextButton = new JButton("ข้อต่อไป");

        JButton previous = new JButton(
                "คำก่อนหน้า (Stack)"
        );

        styleDarkTextButton(start, UITheme.BLUE);
        styleDarkTextButton(checkButton, UITheme.GREEN);
        styleDarkTextButton(nextButton, UITheme.ORANGE);
        styleDarkTextButton(previous, UITheme.DARK_GREEN);

        start.setFont(thaiFont(15, Font.BOLD));
        checkButton.setFont(thaiFont(15, Font.BOLD));
        nextButton.setFont(thaiFont(15, Font.BOLD));
        previous.setFont(thaiFont(15, Font.BOLD));

        start.addActionListener(e -> {

            if (selectedCategory == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "กรุณาเลือกหมวดหมู่ในแท็บบทเรียนก่อน"
                );

                return;
            }

            service.startLesson(selectedCategory);
            loadQuestion();
        });

        checkButton.addActionListener(
                e -> checkAnswer()
        );

        nextButton.addActionListener(
                e -> loadQuestion()
        );

        previous.addActionListener(
                e -> showPreviousWord()
        );

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 8)
        );

        buttons.setOpaque(false);

        buttons.add(start);
        buttons.add(checkButton);
        buttons.add(nextButton);
        buttons.add(previous);

        quizCard.add(title);
        quizCard.add(Box.createVerticalStrut(32));
        quizCard.add(quizWord);
        quizCard.add(Box.createVerticalStrut(22));
        quizCard.add(answerField);
        quizCard.add(Box.createVerticalStrut(15));
        quizCard.add(quizFeedback);
        quizCard.add(Box.createVerticalStrut(20));
        quizCard.add(buttons);

        panel.add(quizCard, BorderLayout.CENTER);

        return panel;
    }

    // ================= LOAD QUESTION =================

    private void loadQuestion() {

        Word word = service.nextQuestion();

        answered = false;
        answerField.setText("");

        if (word == null) {

            quizWord.setText("จบบทเรียนแล้ว!");

            quizFeedback.setText(
                    "คะแนนสะสม " + data.getXp() + " XP"
            );

            refreshStats();
            DataStore.save(data);

            return;
        }

        quizWord.setText(
                "คำแปลของ  “" +
                        word.getEnglish() +
                        "”  คืออะไร?"
        );

        quizFeedback.setText(
                "พิมพ์คำตอบภาษาไทย แล้วกดตรวจคำตอบ"
        );

        checkButton.setEnabled(true);
    }

    // ================= CHECK ANSWER =================

    private void checkAnswer() {

        if (service.getCurrentWord() == null || answered) {
            return;
        }

        boolean correct =
                service.checkAnswer(answerField.getText());

        Word current = service.getCurrentWord();

        if (correct) {

            quizFeedback.setForeground(
                    UITheme.DARK_GREEN
            );

            quizFeedback.setText(
                    "ถูกต้อง! เยี่ยมมาก +10 XP"
            );

        } else {

            quizFeedback.setForeground(
                    new Color(210, 70, 70)
            );

            quizFeedback.setText(
                    "ยังไม่ถูก คำตอบคือ: " +
                            current.getThai()
            );
        }

        answered = true;

        refreshStats();
        DataStore.save(data);
    }

    // ================= PREVIOUS WORD (STACK) =================

    private void showPreviousWord() {

        Word previous = service.getPreviousWord();

        if (previous == null) {

            quizFeedback.setForeground(
                    UITheme.MUTED
            );

            quizFeedback.setText(
                    "ยังไม่มีประวัติคำศัพท์"
            );

            return;
        }

        quizFeedback.setForeground(
                UITheme.BLUE
        );

        quizFeedback.setText(
                "คำก่อนหน้า: " +
                        previous.getEnglish() +
                        " = " +
                        previous.getThai()
        );
    }

    // ================= VOCABULARY =================

    private JPanel createVocabulary() {

        JPanel panel = page();

        JLabel title = UITheme.heading(
                "คลังคำศัพท์", 23
        );
        title.setFont(thaiFont(23, Font.BOLD));

        String[] columns = {
                "English",
                "คำแปล",
                "หมวดหมู่",
                "ตัวอย่างประโยค"
        };

        DefaultTableModel model =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column
                    ) {
                        return false;
                    }
                };

        for (Word word : service.getAllWordsSorted()) {

            model.addRow(new Object[]{

                    word.getEnglish(),
                    word.getThai(),
                    word.getCategory(),
                    word.getExample()

            });
        }

        JTable table = new JTable(model);

        table.setRowHeight(38);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);
        table.setSelectionForeground(UITheme.INK);
        table.setBackground(Color.WHITE);
        table.setForeground(UITheme.INK);

        table.setFont(
                new Font("Tahoma", Font.PLAIN, 13)
        );

        table.getTableHeader().setFont(thaiFont(14, Font.BOLD));
        table.getTableHeader().setBackground(UITheme.PALE_GREEN);
        table.getTableHeader().setForeground(UITheme.DARK_GREEN);
        table.getTableHeader().setPreferredSize(new Dimension(0, 42));

        table.setSelectionBackground(
                UITheme.PALE_GREEN
        );

        JTextField search = new JTextField();

        search.setFont(thaiFont(15, Font.PLAIN));
        search.setBorder(new javax.swing.border.CompoundBorder(
                BorderFactory.createLineBorder(new Color(205, 222, 208), 1, true),
                new EmptyBorder(9, 12, 9, 12)
        ));

        search.setToolTipText(
                "พิมพ์คำศัพท์ภาษาอังกฤษเพื่อค้นหา"
        );

        JButton find = new JButton("ค้นหา");

        styleDarkTextButton(
                find,
                UITheme.GREEN
        );
        find.setFont(thaiFont(15, Font.BOLD));

        find.addActionListener(e -> {

            Word word = service.findWord(
                    search.getText()
            );

            if (word == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "ไม่พบคำศัพท์นี้"
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        word.getEnglish() +
                                " = " +
                                word.getThai() +
                                "\nตัวอย่าง: " +
                                word.getExample()
                );
            }
        });

        JPanel searchPanel = new JPanel(
                new BorderLayout(8, 8)
        );

        searchPanel.setOpaque(false);

        searchPanel.add(
                search,
                BorderLayout.CENTER
        );

        searchPanel.add(
                find,
                BorderLayout.EAST
        );

        panel.add(title, BorderLayout.NORTH);

        panel.add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );

        panel.add(
                searchPanel,
                BorderLayout.SOUTH
        );

        return panel;
    }

    // ================= REVIEW =================

    private JPanel createReview() {

        JPanel panel = page();

        JLabel title = UITheme.heading(
                "คำศัพท์ที่ควรทบทวน", 23
        );
        title.setFont(thaiFont(23, Font.BOLD));

        DefaultListModel<String> model =
                new DefaultListModel<>();

        JList<String> list = new JList<>(model);

        list.setFont(
                new Font("Tahoma", Font.PLAIN, 16)
        );

        list.setFixedCellHeight(42);
        list.setBackground(Color.WHITE);
        list.setSelectionBackground(UITheme.PALE_GREEN);
        list.setSelectionForeground(UITheme.DARK_GREEN);
        list.setBorder(new EmptyBorder(8, 12, 8, 12));

        JButton refresh = new JButton("โหลดรายการ");

        JButton done = new JButton(
                "ทำเครื่องหมายว่าทบทวนแล้ว"
        );

        styleDarkTextButton(
                refresh,
                UITheme.BLUE
        );
        refresh.setFont(thaiFont(14, Font.BOLD));

        styleDarkTextButton(
                done,
                UITheme.GREEN
        );
        done.setFont(thaiFont(14, Font.BOLD));

        refresh.addActionListener(e -> {

            model.clear();

            for (Word word : service.getReviewWords()) {

                model.addElement(
                        word.getEnglish() +
                                " — " +
                                word.getThai()
                );
            }
        });

        done.addActionListener(e -> {

            String selected = list.getSelectedValue();

            if (selected == null) return;

            String english =
                    selected.split(" — ")[0];

            service.removeFromReview(english);

            model.removeElement(selected);

            DataStore.save(data);
        });

        JPanel actions = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        actions.setOpaque(false);

        actions.add(refresh);
        actions.add(done);

        panel.add(title, BorderLayout.NORTH);

        panel.add(
                new JScrollPane(list),
                BorderLayout.CENTER
        );

        panel.add(
                actions,
                BorderLayout.SOUTH
        );

        return panel;
    }

    // ================= HISTORY =================

    private JPanel createHistory() {

        JPanel panel = page();

        JLabel title = UITheme.heading(
                "ประวัติการเรียน", 23
        );
        title.setFont(thaiFont(23, Font.BOLD));

        DefaultListModel<String> model =
                new DefaultListModel<>();

        for (String item : data.getActivityLog()) {
            model.addElement(item);
        }

        JList<String> list = new JList<>(model);

        list.setFont(
                new Font("Tahoma", Font.PLAIN, 14)
        );

        list.setFixedCellHeight(40);
        list.setBackground(Color.WHITE);
        list.setSelectionBackground(UITheme.PALE_GREEN);
        list.setSelectionForeground(UITheme.DARK_GREEN);
        list.setBorder(new EmptyBorder(8, 12, 8, 12));

        JButton refresh = new JButton(
                "รีเฟรชประวัติ"
        );

        styleDarkTextButton(
                refresh,
                UITheme.BLUE
        );
        refresh.setFont(thaiFont(14, Font.BOLD));

        refresh.addActionListener(e -> {

            model.clear();

            for (String item : data.getActivityLog()) {
                model.addElement(item);
            }
        });

        panel.add(title, BorderLayout.NORTH);

        panel.add(
                new JScrollPane(list),
                BorderLayout.CENTER
        );

        panel.add(
                refresh,
                BorderLayout.SOUTH
        );

        return panel;
    }

    // ================= ACCURACY =================

    private int accuracy() {

        if (data.getTotalAnswers() == 0) {
            return 0;
        }

        return (int) Math.round(
                data.getCorrectAnswers() *
                        100.0 /
                        data.getTotalAnswers()
        );
    }

    // ================= REFRESH STATS =================

    private void refreshStats() {

        if (xpValue != null) {
            xpValue.setText(data.getXp() + " XP");
        }

        if (accuracyValue != null) {
            accuracyValue.setText(accuracy() + "%");
        }

        if (lessonValue != null) {

            lessonValue.setText(
                    String.valueOf(
                            data.getLessonsCompleted()
                    )
            );
        }
    }
}