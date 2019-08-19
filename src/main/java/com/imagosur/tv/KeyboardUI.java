package com.imagosur.tv;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import static java.awt.event.KeyEvent.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class KeyboardUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final String[] FN_KEY_OFF_TEXT = new String[]{
        "<html>ª <br />&nbsp;&nbsp;º</html>", "<html>! <br />&nbsp;&nbsp;1</html>", "<html>\" <br />&nbsp;&nbsp;2</html>",
        "<html># <br />&nbsp;&nbsp;3</html>", "<html>$ <br />&nbsp;&nbsp;4</html>", "<html>% <br />&nbsp;&nbsp;5</html>",
        "<html>& <br />&nbsp;&nbsp;6</html>", "<html>/ <br />&nbsp;&nbsp;7</html>", "<html>( <br />&nbsp;&nbsp;8</html>",
        "<html>) <br />&nbsp;&nbsp;9</html>", "<html>= <br />&nbsp;&nbsp;0</html>", "<html>? <br />&nbsp;&nbsp;'</html>",
        "<html>¿ <br />&nbsp;&nbsp;¡</html>"
    };
    private static final String[] FN_KEY_OFF_VK = new String[]{
        "0xC0", "0x31", "0x32", "0x33", "0x34", "0x35", "0x36", "0x37", "0x38", "0x39", "0x30", "0x2D", "0x3D"
    };

    private static final String[] FN_KEY_ON_TEXT = new String[]{
        "Esc", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "F10", "F11", "F12"
    };

    private static final String[] FN_KEY_ON_VK = new String[]{
        "0x1B", "0x70", "0x71", "0x72", "0x73", "0x74", "0x75", "0x76", "0x77", "0x78", "0x79", "0x7A", "0x7B"
    };

    private static final Color SELECTED_COLOR = Color.GRAY;
    private static final Color DEFAULT_KEY_COLOR = new Color(113, 166, 236);
    private static boolean IS_FUNCTION_KEY_PRESSED = false;
    private final ArrayList<JLabel> alphabetKeys;
    private final ArrayList<JLabel> functionKeys;

    /**
     * Creates new GUI form KeyboardUI
     */
    public KeyboardUI(int paraHeigth, int paramWidth) {
        initComponents();
        alphabetKeys = new ArrayList<>();
        functionKeys = new ArrayList<>();

        initKeys();
        setIcon();
        setLocationToBottom(paraHeigth, paramWidth);
    }

    private void initKeys() {
        for(Component component : this.alphabetPanel.getComponents()) {
            try {
                JLabel key = (JLabel)component;
                
                String name = key.getText();
                
                if(name.length()==1) {
                    alphabetKeys.add(key);
                }
            } catch(ClassCastException ex) {
                
            }
        }
        
        for(Component component : this.functionPanel.getComponents()) {
            try {
                JLabel key = (JLabel) component;
                
                functionKeys.add(key);
            } catch(ClassCastException ex) { }
        }
        functionKeys.remove(keybackspace);
    }

    private void setIcon() {
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("tv-icon.png"));
        setIconImage(icon.getImage());
    }

    /**
     * Sets the default location of the Onscreen Keyboard on bottom of the screen 
     * just above the taskbar.
     */
    private void setLocationToBottom(int paramHeigth, int paramWidth) {
        if(paramHeigth == 0 && paramWidth == 0){
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Insets inset = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());

            int workingScreen = screenSize.height - inset.bottom;

            int width = (screenSize.width - getWidth()) / 2; //Positions horizontally to middle of screen
            int height = workingScreen - getHeight(); //Positions vertically just above the taskbar

            setLocation(width, height);
        } else {
            setLocation(paramWidth, paramHeigth);
        }
    }

    /**
     * Performs a key press event on the specific key. Sends a key pressed event 
     * to the current foreground application.
     * @param evt 
     */
    private void keyPressed(MouseEvent evt) {
        JLabel key = (JLabel)evt.getSource();
        key.setBackground(SELECTED_COLOR);
    }

    /**
     * Releases a key that was pressed. Sends a key release event to the current
     * foreground application.
     * @param evt 
     */
    private void keyReleased(MouseEvent evt) {
        JLabel key = (JLabel)evt.getSource();
        key.setBackground(DEFAULT_KEY_COLOR);
    }

    /**
     * Performs a mouse click event on the key. Mouse click generally send a key associated
     * with the source of the event object to the currently focusable foreground application.
     * @param evt 
     */
    private void keyClicked(MouseEvent evt) {
        JLabel key = (JLabel)evt.getSource();           //Source clicked key.
        int keycode = Integer.decode(key.getName());    //Virtual key code associated with the key
        boolean specialkey = false;
        
        /**
         * Check for special keys(e.g. CapsLock, NumLock, Shift key). Perform special
         * actions on special keys click such as pressing, or releasing a pressed key.
         */
        System.out.println("keyClicked - keycode: " + keycode);

        switch(keycode) {
            case VK_SHIFT:
                if (Keyboard.isShiftPressed()) 
                    Keyboard.releaseKey(VK_SHIFT);
                else 
                    Keyboard.pressKey(VK_SHIFT);
                specialkey = true;
                break;
            case VK_CONTROL:
                if (Keyboard.isCtrlPressed())
                    Keyboard.releaseKey(VK_CONTROL);
                else
                    Keyboard.pressKey(VK_CONTROL);
                specialkey = true;
                break;
            case VK_ALT:
                if(Keyboard.isAltPressed())
                    Keyboard.releaseKey(VK_ALT);
                else
                    Keyboard.pressKey(VK_ALT);
                specialkey = true;
                break;
            case VK_CAPS_LOCK:
                Keyboard.typeKey(VK_CAPS_LOCK);
                specialkey = true;
                break;
            case VK_NUM_LOCK:
                Keyboard.typeKey(VK_NUM_LOCK);
                specialkey = true;
                break;
            case VK_SCROLL_LOCK:
                Keyboard.typeKey(VK_SCROLL_LOCK);
                specialkey = true;
                break;
            case 0xff: //Function key pressed
                IS_FUNCTION_KEY_PRESSED = !IS_FUNCTION_KEY_PRESSED;
                specialkey = true;
                break;
        }
        
        if(!specialkey) {
            Keyboard.typeKey(keycode);
            Keyboard.releaseAllSpecialKeys();
        }
        
        updateAlphabeticKeys();
        updateFunctionKeys();
        updateSpecialKeys();
    }

    /**
     * Performs a hover effect on the key button.
     * @param evt 
     */
    private void keyMouseEntered(MouseEvent evt) {
        JLabel source = (JLabel) evt.getSource();
        
        /**
         * Check if the current key is a special key or not.
         * Perform no hover effect if special keys(e.g. CapsLock, NumLock, Shift etc)
         * keys are pressed.
         */
        int keycode = Integer.decode(source.getName());
        switch(keycode) {
            case VK_SHIFT:
                if(Keyboard.isShiftPressed())
                    return;
                break;
            case VK_CONTROL:
                if(Keyboard.isCtrlPressed())
                    return;
                break;
            case VK_ALT:
                if(Keyboard.isAltPressed())
                    return;
                break;
            case VK_CAPS_LOCK:
                if(Keyboard.isCapsLockOn())
                    return;
                break;
            case VK_NUM_LOCK:
                if(Keyboard.isNumLockOn())
                    return;
                break;
            case VK_SCROLL_LOCK:
                if(Keyboard.isScrollLockOn())
                    return;
                break;
            case 0xff: //If the function key is pressed
                if(IS_FUNCTION_KEY_PRESSED)
                    return;
        }
        
        source.setBackground(Color.GRAY);
    }

    /**
     * Performs a hover effect on the key button.
     * @param evt 
     */
    private void keyMouseExited(MouseEvent evt) {
        JLabel source = (JLabel) evt.getSource();
        
        /**
         * Check if the current key is special key or not.
         * Perform no hover effect if the special keys(e.g. CapsLock, NumLock, Shift key etc)
         * are pressed.
         */
        int keycode = Integer.decode(source.getName());
        switch(keycode) {
            case VK_SHIFT:
                if(Keyboard.isShiftPressed())
                    return;
                break;
            case VK_CONTROL:
                if(Keyboard.isCtrlPressed())
                    return;
                break;
            case VK_ALT:
                if(Keyboard.isAltPressed())
                    return;
                break;
            case VK_CAPS_LOCK:
                if(Keyboard.isCapsLockOn())
                    return;
                break;
            case VK_NUM_LOCK:
                if(Keyboard.isNumLockOn())
                    return;
                break;
            case VK_SCROLL_LOCK:
                if(Keyboard.isScrollLockOn())
                    return;
                break;
            case 0xff: //If the function key is pressed
                if(IS_FUNCTION_KEY_PRESSED)
                    return;
        }
        
        source.setBackground(DEFAULT_KEY_COLOR);
    }

    /**
     * Changes the text of the alphabetic keys. This function to be called on every 
     * shift or caps key press or release. This function changes the text of the
     * alphabetic keys to CAPS and to small according to the current status of the
     * Caps lock key and Shift key.
     */
    private void updateAlphabeticKeys() {
        boolean caps =  (Keyboard.isShiftPressed() && !Keyboard.isCapsLockOn()) || 
                        (!Keyboard.isShiftPressed() && Keyboard.isCapsLockOn());
        
        for(JLabel key : alphabetKeys) {
            if(caps)
                key.setText(key.getText().toUpperCase());
            else
                key.setText(key.getText().toLowerCase());
        }
    }

    /**
     * Changes the text of the function/numeric keys. This function is to be called
     * if the function key is clicked. 
     */
    private void updateFunctionKeys() {
        int i = 0;
        for(JLabel key : functionKeys) {
            if(IS_FUNCTION_KEY_PRESSED) {
                key.setText(FN_KEY_ON_TEXT[i]);
                key.setName(FN_KEY_ON_VK[i]);
            } else {
                key.setText(FN_KEY_OFF_TEXT[i]);
                key.setName(FN_KEY_OFF_VK[i]);
            }
            
            i++;
        }
    }

    /**
     * Changes the current state of the special keys(e.g. CapsLock, NumLock, Shift etc).
     * Sets whether the special keys pressed or not. If the special key is pressed 
     * then blue colored keys are shown else normal keys.
     */
    private void updateSpecialKeys() {
        if(Keyboard.isCapsLockOn())
            keycaps.setBackground(SELECTED_COLOR);
        else
            keycaps.setBackground(DEFAULT_KEY_COLOR);

        if(Keyboard.isScrollLockOn())
            keyscrolllock.setBackground(SELECTED_COLOR);
        else
            keyscrolllock.setBackground(DEFAULT_KEY_COLOR);
        
        if(Keyboard.isShiftPressed()) {
            keyshiftl.setBackground(SELECTED_COLOR);
            keyshiftr.setBackground(SELECTED_COLOR);
        } else {
            keyshiftl.setBackground(DEFAULT_KEY_COLOR);
            keyshiftr.setBackground(DEFAULT_KEY_COLOR);
        }
        
        if(Keyboard.isCtrlPressed()) {
            keyctrl1.setBackground(SELECTED_COLOR);
            keyctrl2.setBackground(SELECTED_COLOR); 
        } else {
            keyctrl1.setBackground(DEFAULT_KEY_COLOR);
            keyctrl2.setBackground(DEFAULT_KEY_COLOR);
        }
        
        if(Keyboard.isAltPressed()) {
            keyalt.setBackground(SELECTED_COLOR);
            keyaltgr.setBackground(SELECTED_COLOR);
        } else {
            keyalt.setBackground(DEFAULT_KEY_COLOR);
            keyaltgr.setBackground(DEFAULT_KEY_COLOR);
        }

//        if(IS_FUNCTION_KEY_PRESSED)
//            keyfn.setBackground(SELECTED_COLOR);
//        else
//            keyfn.setBackground(DEFAULT_KEY_COLOR);
    }

    private void initComponents() {

        mainPanel = new JPanel();
        functionPanel = new JPanel();
        keybackquote = initLabel("\"<html>~<br />&nbsp;&nbsp;`</html>\"", "0xC0");
        key1 = initLabel("<html>! <br />&nbsp;&nbsp;1</html>", "0x31");
        key2 = initLabel("<html>\"<br />&nbsp;&nbsp;2</html>", "0x32");
        key3 = initLabel("<html># <br />&nbsp;&nbsp;3</html>", "0x33");
        key4 = initLabel("<html>$ <br />&nbsp;&nbsp;4</html>", "0x34");
        key5 = initLabel("<html>% <br />&nbsp;&nbsp;5</html>", "0x35");
        key6 = initLabel("<html>& <br />&nbsp;&nbsp;6</html>", "0x36");
        key7 = initLabel("<html>/ <br />&nbsp;&nbsp;7</html>", "0x37");
        key8 = initLabel("<html>( <br />&nbsp;&nbsp;8</html>", "0x38");
        key9 = initLabel("<html>) <br />&nbsp;&nbsp;9</html>", "0x39");
        key0 = initLabel("<html>= <br />&nbsp;&nbsp;0</html>", "0x40");
        keyminus = initLabel("<html>_<br />&nbsp;&nbsp;-</html>", "0x2D");
        keyequal = initLabel("<html>+<br />&nbsp;&nbsp;=</html>", "0x3D");
        keybackspace = initLabelWithDimension("Backspace", "0x08", 60, 40);
        alphabetPanel = new JPanel();
        keytab = initLabelWithDimension("TAB", "0x09", 60, 40);
        keyq = initLabel("q", "0x51");
        keyw = initLabel("w", "0x57");
        keye = initLabel("e", "0x45");
        keyr = initLabel("r", "0x52");
        keyt = initLabel("t", "0x54");
        keyy = initLabel("y", "0x59");
        keyu = initLabel("u", "0x55");
        keyi = initLabel("i", "0x49");
        keyo = initLabel("o", "0x4F");
        keyp = initLabel("p", "0x50");
        keyopenbigbracket = initLabel("<html>{<br />&nbsp;&nbsp;[</html>", "0x5B");
        keyclosebigbracket = initLabel("<html>}<br />&nbsp;&nbsp;]</html>", "0x5D");
        keybslash = initLabel("<html>|<br />&nbsp;&nbsp;\\\\</html>", "0x5C");
        keyj = initLabel("j", "0x4A");
        keyk = initLabel("k", "0x4B");
        keyl = initLabel("l", "0x4C");
        keycaps = initLabelWithDimension("caps", "0x14", 70, 40);
        keysemicolon = initLabel("<html>:<br />&nbsp;&nbsp;;</html>", "0x3B");
        keyquote = initLabel("<html>\"<br />&nbsp;&nbsp;'</html>", "0xDE");
        keya = initLabel("a", "0x41");
        keyenter = initLabelWithDimension("Enter", "0x0A", 76, 40);
        keys = initLabel("s", "0x53");
        keyd = initLabel("d", "0x44");
        keyf = initLabel("f", "0x46");
        keyg = initLabel("g", "0x47");
        keyh = initLabel("h", "0x48");
        keym = initLabel("m", "0x4D");
//        keycomma = initLabel("<html>&lt;<br />&nbsp;&nbsp;,</html>", "0x2C");
//        keydot = initLabel("<html>&gt;<br />&nbsp;&nbsp;.</html>", "0x2E");
        keycomma = new JLabel();
        keydot = new JLabel();
        keyshiftl = initLabelWithDimension("Shift", "0x10", 87, 40);
        keyfslash = initLabel("/", "0x2F");
        keyz = initLabel("z", "0x5A");
        keyshiftr = initLabel("Shift", "0x10");
        keyx = initLabel("x", "0x58");
        keyc = initLabel("c", "0x43");
        keyv = initLabel("v", "0x56");
        keyb = initLabel("b", "0x42");
        keyn = initLabel("n", "0x4E");
        keyrightarrow = initLabel(">", "0x27");
        keyctrl1 = initLabel("Ctrl", "0x11");
        keycommercialat = initLabel("@", "0x200");
        keyalt = initLabel("Alt", "0x12");
        keyspace = initLabelWithDimension(" ", "0x20", 244, 40);
        keyctrl2 = initLabel("Ctrl", "0x11");
        keyleftarrow = initLabel("<", "0x26");
        keyuparrow = initLabelWithDimension("^", "0x26", 40, 19);
        keydownarrow = initLabelWithDimension(" ", "0x28", 40, 19);
        keyaltgr = initLabel("ALT GR", "0x12");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Teclado Virtual");
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setBackground(Color.GRAY);
        setFocusable(false);
        setFocusableWindowState(false);
        setResizable(false);

        mainPanel.setBackground(new Color(0, 123, 253));
        functionPanel.setBackground(new Color(0,123, 253));
        functionPanel.setFocusable(false);

//        keybackquote.setBackground(DEFAULT_KEY_COLOR);
//        keybackquote.setForeground(java.awt.Color.white);
//        keybackquote.setHorizontalAlignment(SwingConstants.CENTER);
//        keybackquote.setText("<html>~<br />&nbsp;&nbsp;`</html>");
//        keybackquote.setFocusable(false);
//        keybackquote.setMaximumSize(new java.awt.Dimension(40, 40));
//        keybackquote.setMinimumSize(new java.awt.Dimension(40, 40));
//        keybackquote.setName("0xC0"); // NOI18N
//        keybackquote.setOpaque(true);
//        keybackquote.setPreferredSize(new java.awt.Dimension(40, 40));
//        keybackquote.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keyminus.setBackground(DEFAULT_KEY_COLOR);
//        keyminus.setForeground(java.awt.Color.white);
//        keyminus.setHorizontalAlignment(SwingConstants.CENTER);
//        keyminus.setText("<html>_<br />&nbsp;&nbsp;-</html>");
//        keyminus.setFocusable(false);
//        keyminus.setMaximumSize(new java.awt.Dimension(40, 40));
//        keyminus.setMinimumSize(new java.awt.Dimension(40, 40));
//        keyminus.setName("0x2D"); // NOI18N
//        keyminus.setOpaque(true);
//        keyminus.setPreferredSize(new java.awt.Dimension(40, 40));
//        keyminus.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keyequal.setBackground(DEFAULT_KEY_COLOR);
//        keyequal.setForeground(java.awt.Color.white);
//        keyequal.setHorizontalAlignment(SwingConstants.CENTER);
//        keyequal.setText("<html>+<br />&nbsp;&nbsp;=</html>");
//        keyequal.setFocusable(false);
//        keyequal.setMaximumSize(new java.awt.Dimension(40, 40));
//        keyequal.setMinimumSize(new java.awt.Dimension(40, 40));
//        keyequal.setName("0x3D"); // NOI18N
//        keyequal.setOpaque(true);
//        keyequal.setPreferredSize(new java.awt.Dimension(40, 40));
//        keyequal.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });
//
//        keybackspace.setBackground(DEFAULT_KEY_COLOR);
//        keybackspace.setForeground(java.awt.Color.white);
//        keybackspace.setHorizontalAlignment(SwingConstants.CENTER);
//        keybackspace.setText("Backspace");
//        keybackspace.setFocusable(false);
//        keybackspace.setMaximumSize(new java.awt.Dimension(60, 40));
//        keybackspace.setMinimumSize(new java.awt.Dimension(60, 40));
//        keybackspace.setName("0x08"); // NOI18N
//        keybackspace.setOpaque(true);
//        keybackspace.setPreferredSize(new java.awt.Dimension(60, 40));
//        keybackspace.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

        GroupLayout functionPanelLayout = new GroupLayout(functionPanel);
        functionPanel.setLayout(functionPanelLayout);
        functionPanelLayout.setHorizontalGroup(
            functionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(functionPanelLayout.createSequentialGroup()
                .addComponent(keybackquote, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(key1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(key2, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(key3, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(key4, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(key5, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(key6, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(key7, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(key8, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(key9, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(key0, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(keyminus, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(keyequal, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(keybackspace, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
        );
        functionPanelLayout.setVerticalGroup(
            functionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(functionPanelLayout.createSequentialGroup()
                .addGroup(functionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(keybackquote, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(key2, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(key3, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(key4, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(key5, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(key6, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(key7, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(key8, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(key9, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(key0, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(keyminus, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(keyequal, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(keybackspace, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(key1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        alphabetPanel.setBackground(new Color(0, 123, 253));
        alphabetPanel.setFocusable(false);

//        keytab.setBackground(DEFAULT_KEY_COLOR);
//        keytab.setForeground(java.awt.Color.white);
//        keytab.setHorizontalAlignment(SwingConstants.LEFT);
//        keytab.setText(" Tab");
//        keytab.setFocusable(false);
//        keytab.setMaximumSize(new java.awt.Dimension(60, 40));
//        keytab.setMinimumSize(new java.awt.Dimension(60, 40));
//        keytab.setName("0x09"); // NOI18N
//        keytab.setOpaque(true);
//        keytab.setPreferredSize(new java.awt.Dimension(60, 40));
//        keytab.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keyopenbigbracket.setBackground(DEFAULT_KEY_COLOR);
//        keyopenbigbracket.setForeground(java.awt.Color.white);
//        keyopenbigbracket.setHorizontalAlignment(SwingConstants.CENTER);
//        keyopenbigbracket.setText("<html>{<br />&nbsp;&nbsp;[</html>");
//        keyopenbigbracket.setFocusable(false);
//        keyopenbigbracket.setMaximumSize(new java.awt.Dimension(40, 40));
//        keyopenbigbracket.setMinimumSize(new java.awt.Dimension(40, 40));
//        keyopenbigbracket.setName("0x5B"); // NOI18N
//        keyopenbigbracket.setOpaque(true);
//        keyopenbigbracket.setPreferredSize(new java.awt.Dimension(40, 40));
//        keyopenbigbracket.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keyclosebigbracket.setBackground(DEFAULT_KEY_COLOR);
//        keyclosebigbracket.setForeground(java.awt.Color.white);
//        keyclosebigbracket.setHorizontalAlignment(SwingConstants.CENTER);
//        keyclosebigbracket.setText("<html>}<br />&nbsp;&nbsp;]</html>");
//        keyclosebigbracket.setFocusable(false);
//        keyclosebigbracket.setMaximumSize(new java.awt.Dimension(40, 40));
//        keyclosebigbracket.setMinimumSize(new java.awt.Dimension(40, 40));
//        keyclosebigbracket.setName("0x5D"); // NOI18N
//        keyclosebigbracket.setOpaque(true);
//        keyclosebigbracket.setPreferredSize(new java.awt.Dimension(40, 40));
//        keyclosebigbracket.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keybslash.setBackground(DEFAULT_KEY_COLOR);
//        keybslash.setForeground(java.awt.Color.white);
//        keybslash.setHorizontalAlignment(SwingConstants.CENTER);
//        keybslash.setText("<html>|<br />&nbsp;&nbsp;\\</html>");
//        keybslash.setFocusable(false);
//        keybslash.setMaximumSize(new java.awt.Dimension(40, 40));
//        keybslash.setMinimumSize(new java.awt.Dimension(40, 40));
//        keybslash.setName("0x5C"); // NOI18N
//        keybslash.setOpaque(true);
//        keybslash.setPreferredSize(new java.awt.Dimension(40, 40));
//        keybslash.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keycaps.setBackground(DEFAULT_KEY_COLOR);
//        keycaps.setForeground(java.awt.Color.white);
//        keycaps.setHorizontalAlignment(SwingConstants.LEFT);
//        keycaps.setText(" CapsLock");
//        keycaps.setFocusable(false);
//        keycaps.setMaximumSize(new java.awt.Dimension(70, 40));
//        keycaps.setMinimumSize(new java.awt.Dimension(70, 40));
//        keycaps.setName("0x14"); // NOI18N
//        keycaps.setOpaque(true);
//        keycaps.setPreferredSize(new java.awt.Dimension(70, 40));
//        keycaps.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keysemicolon.setBackground(DEFAULT_KEY_COLOR);
//        keysemicolon.setForeground(java.awt.Color.white);
//        keysemicolon.setHorizontalAlignment(SwingConstants.CENTER);
//        keysemicolon.setText("<html>:<br />&nbsp;&nbsp;;</html>");
//        keysemicolon.setFocusable(false);
//        keysemicolon.setMaximumSize(new java.awt.Dimension(40, 40));
//        keysemicolon.setMinimumSize(new java.awt.Dimension(40, 40));
//        keysemicolon.setName("0x3B"); // NOI18N
//        keysemicolon.setOpaque(true);
//        keysemicolon.setPreferredSize(new java.awt.Dimension(40, 40));
//        keysemicolon.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keyquote.setBackground(DEFAULT_KEY_COLOR);
//        keyquote.setForeground(java.awt.Color.white);
//        keyquote.setHorizontalAlignment(SwingConstants.CENTER);
//        keyquote.setText("<html>\"<br />&nbsp;&nbsp;'</html>");
//        keyquote.setFocusable(false);
//        keyquote.setMaximumSize(new java.awt.Dimension(40, 40));
//        keyquote.setMinimumSize(new java.awt.Dimension(40, 40));
//        keyquote.setName("0xDE"); // NOI18N
//        keyquote.setOpaque(true);
//        keyquote.setPreferredSize(new java.awt.Dimension(40, 40));
//        keyquote.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keyenter.setBackground(DEFAULT_KEY_COLOR);
//        keyenter.setForeground(java.awt.Color.white);
//        keyenter.setHorizontalAlignment(SwingConstants.RIGHT);
//        keyenter.setText("Enter ");
//        keyenter.setFocusable(false);
//        keyenter.setMaximumSize(new java.awt.Dimension(76, 40));
//        keyenter.setMinimumSize(new java.awt.Dimension(76, 40));
//        keyenter.setName("0x0A"); // NOI18N
//        keyenter.setOpaque(true);
//        keyenter.setPreferredSize(new java.awt.Dimension(76, 40));
//        keyenter.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

        keycomma.setBackground(DEFAULT_KEY_COLOR);
        keycomma.setForeground(java.awt.Color.white);
        keycomma.setHorizontalAlignment(SwingConstants.CENTER);
        keycomma.setText("<html>&lt;<br />&nbsp;&nbsp;,</html>");
        keycomma.setFocusable(false);
        keycomma.setMaximumSize(new java.awt.Dimension(40, 40));
        keycomma.setMinimumSize(new java.awt.Dimension(40, 40));
        keycomma.setName("0x2C"); // NOI18N
        keycomma.setOpaque(true);
        keycomma.setPreferredSize(new java.awt.Dimension(40, 40));
        keycomma.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                keyClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                keyMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                keyMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                keyPressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                keyReleased(evt);
            }
        });

        keydot.setBackground(DEFAULT_KEY_COLOR);
        keydot.setForeground(java.awt.Color.white);
        keydot.setHorizontalAlignment(SwingConstants.CENTER);
        keydot.setText("<html>&gt;<br />&nbsp;&nbsp;.</html>");
        keydot.setFocusable(false);
        keydot.setMaximumSize(new java.awt.Dimension(40, 40));
        keydot.setMinimumSize(new java.awt.Dimension(40, 40));
        keydot.setName("0x2E"); // NOI18N
        keydot.setOpaque(true);
        keydot.setPreferredSize(new java.awt.Dimension(40, 40));
        keydot.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                keyClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                keyMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                keyMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                keyPressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                keyReleased(evt);
            }
        });

//        keyshiftl.setBackground(DEFAULT_KEY_COLOR);
//        keyshiftl.setForeground(java.awt.Color.white);
//        keyshiftl.setHorizontalAlignment(SwingConstants.LEFT);
//        keyshiftl.setText(" Shift");
//        keyshiftl.setFocusable(false);
//        keyshiftl.setMaximumSize(new java.awt.Dimension(87, 40));
//        keyshiftl.setMinimumSize(new java.awt.Dimension(87, 40));
//        keyshiftl.setName("0x10"); // NOI18N
//        keyshiftl.setOpaque(true);
//        keyshiftl.setPreferredSize(new java.awt.Dimension(87, 40));
//        keyshiftl.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keyfslash.setBackground(DEFAULT_KEY_COLOR);
//        keyfslash.setForeground(java.awt.Color.white);
//        keyfslash.setHorizontalAlignment(SwingConstants.CENTER);
//        keyfslash.setText("<html>?<br />&nbsp;&nbsp;/</html>");
//        keyfslash.setFocusable(false);
//        keyfslash.setMaximumSize(new java.awt.Dimension(40, 40));
//        keyfslash.setMinimumSize(new java.awt.Dimension(40, 40));
//        keyfslash.setName("0x2F"); // NOI18N
//        keyfslash.setOpaque(true);
//        keyfslash.setPreferredSize(new java.awt.Dimension(40, 40));
//        keyfslash.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keyshiftr.setBackground(DEFAULT_KEY_COLOR);
//        keyshiftr.setForeground(java.awt.Color.white);
//        keyshiftr.setHorizontalAlignment(SwingConstants.RIGHT);
//        keyshiftr.setText("Shift ");
//        keyshiftr.setFocusable(false);
//        keyshiftr.setMaximumSize(new java.awt.Dimension(105, 40));
//        keyshiftr.setMinimumSize(new java.awt.Dimension(105, 40));
//        keyshiftr.setName("0x10"); // NOI18N
//        keyshiftr.setOpaque(true);
//        keyshiftr.setPreferredSize(new java.awt.Dimension(105, 40));
//        keyshiftr.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keyrightarrow.setBackground(DEFAULT_KEY_COLOR);
//        keyrightarrow.setForeground(java.awt.Color.white);
//        keyrightarrow.setHorizontalAlignment(SwingConstants.CENTER);
//        keyrightarrow.setText(">");
//        keyrightarrow.setFocusable(false);
//        keyrightarrow.setMaximumSize(new java.awt.Dimension(40, 40));
//        keyrightarrow.setMinimumSize(new java.awt.Dimension(40, 40));
//        keyrightarrow.setName("0x27"); // NOI18N
//        keyrightarrow.setOpaque(true);
//        keyrightarrow.setPreferredSize(new java.awt.Dimension(40, 40));
//        keyrightarrow.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keyctrl1.setBackground(DEFAULT_KEY_COLOR);
//        keyctrl1.setForeground(java.awt.Color.white);
//        keyctrl1.setHorizontalAlignment(SwingConstants.CENTER);
//        keyctrl1.setText("Ctrl");
//        keyctrl1.setFocusable(false);
//        keyctrl1.setMaximumSize(new java.awt.Dimension(40, 40));
//        keyctrl1.setMinimumSize(new java.awt.Dimension(40, 40));
//        keyctrl1.setName("0x11"); // NOI18N
//        keyctrl1.setOpaque(true);
//        keyctrl1.setPreferredSize(new java.awt.Dimension(40, 40));
//        keyctrl1.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keycommercialat.setBackground(DEFAULT_KEY_COLOR);
//        keycommercialat.setForeground(java.awt.Color.white);
//        keycommercialat.setHorizontalAlignment(SwingConstants.CENTER);
//        keycommercialat.setText("@");
//        keycommercialat.setFocusable(false);
//        keycommercialat.setMaximumSize(new java.awt.Dimension(60, 40));
//        keycommercialat.setMinimumSize(new java.awt.Dimension(60, 40));
//        keycommercialat.setName("0x200"); // NOI18N
//        keycommercialat.setOpaque(true);
//        keycommercialat.setPreferredSize(new java.awt.Dimension(60, 40));
//        keycommercialat.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keyalt.setBackground(DEFAULT_KEY_COLOR);
//        keyalt.setForeground(java.awt.Color.white);
//        keyalt.setHorizontalAlignment(SwingConstants.CENTER);
//        keyalt.setText("Alt");
//        keyalt.setFocusable(false);
//        keyalt.setMaximumSize(new java.awt.Dimension(40, 40));
//        keyalt.setMinimumSize(new java.awt.Dimension(40, 40));
//        keyalt.setName("0x12"); // NOI18N
//        keyalt.setOpaque(true);
//        keyalt.setPreferredSize(new java.awt.Dimension(40, 40));
//        keyalt.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keyspace.setBackground(DEFAULT_KEY_COLOR);
//        keyspace.setForeground(java.awt.Color.white);
//        keyspace.setHorizontalAlignment(SwingConstants.CENTER);
//        keyspace.setText(" ");
//        keyspace.setFocusable(false);
//        keyspace.setMaximumSize(new java.awt.Dimension(244, 40));
//        keyspace.setMinimumSize(new java.awt.Dimension(244, 40));
//        keyspace.setName("0x20"); // NOI18N
//        keyspace.setOpaque(true);
//        keyspace.setPreferredSize(new java.awt.Dimension(244, 40));
//        keyspace.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keyctrl2.setBackground(DEFAULT_KEY_COLOR);
//        keyctrl2.setForeground(java.awt.Color.white);
//        keyctrl2.setHorizontalAlignment(SwingConstants.CENTER);
//        keyctrl2.setText("Ctrl");
//        keyctrl2.setFocusable(false);
//        keyctrl2.setMaximumSize(new java.awt.Dimension(40, 40));
//        keyctrl2.setMinimumSize(new java.awt.Dimension(40, 40));
//        keyctrl2.setName("0x11"); // NOI18N
//        keyctrl2.setOpaque(true);
//        keyctrl2.setPreferredSize(new java.awt.Dimension(40, 40));
//        keyctrl2.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keyleftarrow.setBackground(DEFAULT_KEY_COLOR);
//        keyleftarrow.setForeground(java.awt.Color.white);
//        keyleftarrow.setHorizontalAlignment(SwingConstants.CENTER);
//        keyleftarrow.setText("<");
//        keyleftarrow.setFocusable(false);
//        keyleftarrow.setMaximumSize(new java.awt.Dimension(40, 40));
//        keyleftarrow.setMinimumSize(new java.awt.Dimension(40, 40));
//        keyleftarrow.setName("0x25"); // NOI18N
//        keyleftarrow.setOpaque(true);
//        keyleftarrow.setPreferredSize(new java.awt.Dimension(40, 40));
//        keyleftarrow.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keyuparrow.setBackground(DEFAULT_KEY_COLOR);
//        keyuparrow.setForeground(java.awt.Color.white);
//        keyuparrow.setHorizontalAlignment(SwingConstants.CENTER);
//        keyuparrow.setText("↑");
//        keyuparrow.setFocusable(false);
//        keyuparrow.setMaximumSize(new java.awt.Dimension(40, 19));
//        keyuparrow.setMinimumSize(new java.awt.Dimension(40, 19));
//        keyuparrow.setName("0x26"); // NOI18N
//        keyuparrow.setOpaque(true);
//        keyuparrow.setPreferredSize(new java.awt.Dimension(40, 19));
//        keyuparrow.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keydownarrow.setBackground(DEFAULT_KEY_COLOR);
//        keydownarrow.setForeground(java.awt.Color.white);
//        keydownarrow.setHorizontalAlignment(SwingConstants.CENTER);
//        keydownarrow.setText("↓");
//        keydownarrow.setFocusable(false);
//        keydownarrow.setMaximumSize(new java.awt.Dimension(40, 19));
//        keydownarrow.setMinimumSize(new java.awt.Dimension(40, 19));
//        keydownarrow.setName("0x28"); // NOI18N
//        keydownarrow.setOpaque(true);
//        keydownarrow.setPreferredSize(new java.awt.Dimension(40, 19));
//        keydownarrow.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

//        keyaltgr.setBackground(DEFAULT_KEY_COLOR);
//        keyaltgr.setForeground(java.awt.Color.white);
//        keyaltgr.setHorizontalAlignment(SwingConstants.CENTER);
//        keyaltgr.setText("AltGr");
//        keyaltgr.setFocusable(false);
//        keyaltgr.setMaximumSize(new java.awt.Dimension(40, 40));
//        keyaltgr.setMinimumSize(new java.awt.Dimension(40, 40));
//        keyaltgr.setName("0x12"); // NOI18N
//        keyaltgr.setOpaque(true);
//        keyaltgr.setPreferredSize(new java.awt.Dimension(40, 40));
//        keyaltgr.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                keyClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                keyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                keyMouseExited(evt);
//            }
//            public void mousePressed(java.awt.event.MouseEvent evt) {
//                keyPressed(evt);
//            }
//            public void mouseReleased(java.awt.event.MouseEvent evt) {
//                keyReleased(evt);
//            }
//        });

        GroupLayout alphabetPanelLayout = new GroupLayout(alphabetPanel);
        alphabetPanel.setLayout(alphabetPanelLayout);
        alphabetPanelLayout.setHorizontalGroup(
            alphabetPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(alphabetPanelLayout.createSequentialGroup()
                .addGroup(alphabetPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(alphabetPanelLayout.createSequentialGroup()
                        .addComponent(keycaps, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keya, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keys, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyd, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyf, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyg, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyh, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyj, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyk, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyl, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keysemicolon, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyquote, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyenter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGroup(alphabetPanelLayout.createSequentialGroup()
                        .addComponent(keytab, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyq, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyw, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keye, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyr, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyt, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyy, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyu, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyi, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyo, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyp, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyopenbigbracket, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyclosebigbracket, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keybslash, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(GroupLayout.Alignment.TRAILING, alphabetPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(alphabetPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(alphabetPanelLayout.createSequentialGroup()
                        .addComponent(keyctrl1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keycommercialat, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyalt, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyspace, GroupLayout.PREFERRED_SIZE, 238, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyaltgr, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyctrl2, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyleftarrow, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addGroup(alphabetPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(keyuparrow, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                            .addComponent(keydownarrow, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addComponent(keyrightarrow, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                    .addGroup(alphabetPanelLayout.createSequentialGroup()
                        .addComponent(keyshiftl, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyz, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyx, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyc, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyv, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyb, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keym, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keycomma, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keydot, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyfslash, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyshiftr, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)))
                .addGap(17, 17, 17))
        );
        alphabetPanelLayout.setVerticalGroup(
            alphabetPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(alphabetPanelLayout.createSequentialGroup()
                .addGroup(alphabetPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(alphabetPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(keytab, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyq, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyw, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keye, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyr, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyt, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyy, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyu, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyi, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyo, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyp, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                    .addGroup(alphabetPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(keybslash, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyclosebigbracket, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyopenbigbracket, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4)
                .addGroup(alphabetPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(alphabetPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(keycaps, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keya, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keys, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyd, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyf, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyg, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyh, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyj, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyk, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyl, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyenter, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                    .addGroup(alphabetPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(keysemicolon, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyquote, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4)
                .addGroup(alphabetPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(alphabetPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(keyshiftl, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyz, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyx, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyc, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyv, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyb, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keym, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyshiftr, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                    .addGroup(alphabetPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(keycomma, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keydot, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyfslash, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4)
                .addGroup(alphabetPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(keycommercialat, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addGroup(alphabetPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(keyctrl1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyalt, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyspace, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyctrl2, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyleftarrow, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyaltgr, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                    .addComponent(keyuparrow, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
                    .addComponent(keyrightarrow, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(keydownarrow, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)))
        );

        GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(functionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(alphabetPanel, GroupLayout.PREFERRED_SIZE, 632, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGap(0, 0, 0))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(functionPanel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(alphabetPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }

    private JLabel initLabel(String text, String name) {
      return initLabelWithDimension(text, name, 40, 40);
    }

    private JLabel initLabelWithDimension(String text, String name, int width, int height) {
      JLabel label = new JLabel();
      label.setBackground(DEFAULT_KEY_COLOR);
      label.setForeground(Color.white);
      label.setHorizontalAlignment(SwingConstants.CENTER);
      label.setText(text);
      label.setFocusable(false);
      label.setMaximumSize(new Dimension(width, height));
      label.setMinimumSize(new Dimension(width, height));
      label.setName(name);
      label.setOpaque(true);
      label.setPreferredSize(new Dimension(width, height));
      label.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent evt) {
              keyClicked(evt);
          }
          @Override
          public void mouseEntered(MouseEvent evt) {
              keyMouseEntered(evt);
          }
          @Override
          public void mouseExited(MouseEvent evt) {
              keyMouseExited(evt);
          }
          @Override
          public void mousePressed(MouseEvent evt) {
              keyPressed(evt);
          }
          @Override
          public void mouseReleased(MouseEvent evt) {
              keyReleased(evt);
          }
      });
      return label;
    }

    private JPanel alphabetPanel;

    private JPanel functionPanel;

    private JLabel key0;
    private JLabel key1;
    private JLabel key2;
    private JLabel key3;
    private JLabel key4;
    private JLabel key5;
    private JLabel key6;
    private JLabel key7;
    private JLabel key8;
    private JLabel key9;
    private JLabel keya;
    private JLabel keyalt;
    private JLabel keyaltgr;
    private JLabel keyb;
    private JLabel keybackquote;
    private JLabel keybackspace;
    private JLabel keybslash;
    private JLabel keyc;
    private JLabel keycaps;
    private JLabel keyclosebigbracket;
    private JLabel keycomma;
    private JLabel keyctrl1;
    private JLabel keyctrl2;
    private JLabel keyd;
    private JLabel keydot;
    private JLabel keydownarrow;
    private JLabel keye;
    private JLabel keyenter;
    private JLabel keyequal;
    private JLabel keyf;
    private JLabel keyfslash;
    private JLabel keyg;
    private JLabel keyh;
    private JLabel keyi;
    private JLabel keyj;
    private JLabel keyk;
    private JLabel keyl;
    private JLabel keyleftarrow;
    private JLabel keym;
    private JLabel keyminus;
    private JLabel keyn;
    private JLabel keyo;
    private JLabel keyopenbigbracket;
    private JLabel keyp;
    private JLabel keyq;
    private JLabel keyquote;
    private JLabel keyr;
    private JLabel keyrightarrow;
    private JLabel keys;
    private JLabel keyscrolllock;
    private JLabel keysemicolon;
    private JLabel keyshiftl;
    private JLabel keyshiftr;
    private JLabel keyspace;
    private JLabel keyt;
    private JLabel keytab;
    private JLabel keyu;
    private JLabel keyuparrow;
    private JLabel keyv;
    private JLabel keyw;
    private JLabel keycommercialat;
    private JLabel keyx;
    private JLabel keyy;
    private JLabel keyz;
    private JPanel mainPanel;

}
