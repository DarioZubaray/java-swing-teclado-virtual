package com.imagosur.tv;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import static java.awt.event.KeyEvent.*;
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
        "<html>~<br />&nbsp;&nbsp;`</html>", "<html>!<br />&nbsp;&nbsp;1</html>", "<html>@<br />&nbsp;&nbsp;2</html>",
        "<html>#<br />&nbsp;&nbsp;3</html>", "<html>$<br />&nbsp;&nbsp;4</html>", "<html>%<br />&nbsp;&nbsp;5</html>",
        "<html>^<br />&nbsp;&nbsp;6</html>", "<html>&<br />&nbsp;&nbsp;7</html>", "<html>*<br />&nbsp;&nbsp;8</html>",
        "<html>(<br />&nbsp;&nbsp;9</html>", "<html>)<br />&nbsp;&nbsp;0</html>", "<html>_<br />&nbsp;&nbsp;-</html>",
        "<html>+<br />&nbsp;&nbsp;=</html>"
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

    private static final Color SELECTED_COLOR = new Color(-15304792); //Dark Sky blue
    private static final Color DEFAULT_KEY_COLOR = new Color(-13421773); //Color GRAY
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
        
        if(Keyboard.isNumLockOn())
            keynumlock.setBackground(SELECTED_COLOR);
        else
            keynumlock.setBackground(DEFAULT_KEY_COLOR);
        
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

        if(IS_FUNCTION_KEY_PRESSED)
            keyfn.setBackground(SELECTED_COLOR);
        else
            keyfn.setBackground(DEFAULT_KEY_COLOR);
    }

    private void initComponents() {

        mainPanel = new JPanel();
//        numpadPanel = new JPanel();
        keyprtscr = new JLabel();
        keynumdivide = new JLabel();
        keynummult = new JLabel();
        keynumminus = new JLabel();
        keynum7 = new JLabel();
        keynum8 = new JLabel();
        keynum9 = new JLabel();
        keynumplus = new JLabel();
        keynum5 = new JLabel();
        keynum6 = new JLabel();
        keynum4 = new JLabel();
        keynum3 = new JLabel();
        keynum2 = new JLabel();
        keynum1 = new JLabel();
        keynumenter = new JLabel();
        keynumdot = new JLabel();
        keynum0 = new JLabel();
        functionPanel = new JPanel();
        keybackquote = new JLabel();
        key1 = new JLabel();
        key2 = new JLabel();
        key3 = new JLabel();
        key4 = new JLabel();
        key5 = new JLabel();
        key6 = new JLabel();
        key7 = new JLabel();
        key8 = new JLabel();
        key9 = new JLabel();
        key0 = new JLabel();
        keyminus = new JLabel();
        keyequal = new JLabel();
        keybackspace = new JLabel();
        alphabetPanel = new JPanel();
        keytab = new JLabel();
        keyq = new JLabel();
        keyw = new JLabel();
        keye = new JLabel();
        keyr = new JLabel();
        keyt = new JLabel();
        keyy = new JLabel();
        keyu = new JLabel();
        keyi = new JLabel();
        keyo = new JLabel();
        keyp = new JLabel();
        keyopenbigbracket = new JLabel();
        keyclosebigbracket = new JLabel();
        keybslash = new JLabel();
        keyj = new JLabel();
        keyk = new JLabel();
        keyl = new JLabel();
        keycaps = new JLabel();
        keysemicolon = new JLabel();
        keyquote = new JLabel();
        keya = new JLabel();
        keyenter = new JLabel();
        keys = new JLabel();
        keyd = new JLabel();
        keyf = new JLabel();
        keyg = new JLabel();
        keyh = new JLabel();
        keym = new JLabel();
        keycomma = new JLabel();
        keydot = new JLabel();
        keyshiftl = new JLabel();
        keyfslash = new JLabel();
        keyz = new JLabel();
        keyshiftr = new JLabel();
        keyx = new JLabel();
        keyc = new JLabel();
        keyv = new JLabel();
        keyb = new JLabel();
        keyn = new JLabel();
        keyrightarrow = new JLabel();
        keyctrl1 = new JLabel();
        keyfn = new JLabel();
        keycommercialat = new JLabel();
        keyalt = new JLabel();
        keyspace = new JLabel();
        keyctrl2 = new JLabel();
        keyleftarrow = new JLabel();
        keyuparrow = new JLabel();
        keydownarrow = new JLabel();
        keyaltgr = new JLabel();
//        optionPanel = new JPanel();
        keyscrolllock = new JLabel();
        keynumlock = new JLabel();
        keyinsert = new JLabel();
        keydelete = new JLabel();
        keyend = new JLabel();
        keyhome = new JLabel();
        keypgdn = new JLabel();
        keypgup = new JLabel();
//        copyright = new JLabel();
//        numericpadCombobox = new JCheckBox();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Teclado Virtual");
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setBackground(java.awt.Color.black);
        setFocusable(false);
        setFocusableWindowState(false);
        setResizable(false);

        mainPanel.setBackground(java.awt.Color.black);

//        numpadPanel.setBackground(java.awt.Color.black);

        keyprtscr.setBackground(DEFAULT_KEY_COLOR);
        keyprtscr.setForeground(java.awt.Color.white);
        keyprtscr.setHorizontalAlignment(SwingConstants.CENTER);
        keyprtscr.setText("PrtScr");
        keyprtscr.setFocusable(false);
        keyprtscr.setMaximumSize(new java.awt.Dimension(40, 40));
        keyprtscr.setMinimumSize(new java.awt.Dimension(40, 40));
        keyprtscr.setName("0x9A"); // NOI18N
        keyprtscr.setOpaque(true);
        keyprtscr.setPreferredSize(new java.awt.Dimension(40, 40));
        keyprtscr.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keynumdivide.setBackground(DEFAULT_KEY_COLOR);
        keynumdivide.setForeground(java.awt.Color.white);
        keynumdivide.setHorizontalAlignment(SwingConstants.CENTER);
        keynumdivide.setText("/");
        keynumdivide.setFocusable(false);
        keynumdivide.setMaximumSize(new java.awt.Dimension(40, 40));
        keynumdivide.setMinimumSize(new java.awt.Dimension(40, 40));
        keynumdivide.setName("0x6F"); // NOI18N
        keynumdivide.setOpaque(true);
        keynumdivide.setPreferredSize(new java.awt.Dimension(40, 40));
        keynumdivide.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keynummult.setBackground(DEFAULT_KEY_COLOR);
        keynummult.setForeground(java.awt.Color.white);
        keynummult.setHorizontalAlignment(SwingConstants.CENTER);
        keynummult.setText("*");
        keynummult.setFocusable(false);
        keynummult.setMaximumSize(new java.awt.Dimension(40, 40));
        keynummult.setMinimumSize(new java.awt.Dimension(40, 40));
        keynummult.setName("0x6A"); // NOI18N
        keynummult.setOpaque(true);
        keynummult.setPreferredSize(new java.awt.Dimension(40, 40));
        keynummult.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keynumminus.setBackground(DEFAULT_KEY_COLOR);
        keynumminus.setForeground(java.awt.Color.white);
        keynumminus.setHorizontalAlignment(SwingConstants.CENTER);
        keynumminus.setText("-");
        keynumminus.setFocusable(false);
        keynumminus.setMaximumSize(new java.awt.Dimension(40, 40));
        keynumminus.setMinimumSize(new java.awt.Dimension(40, 40));
        keynumminus.setName("0x2D"); // NOI18N
        keynumminus.setOpaque(true);
        keynumminus.setPreferredSize(new java.awt.Dimension(40, 40));
        keynumminus.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keynum7.setBackground(DEFAULT_KEY_COLOR);
        keynum7.setForeground(java.awt.Color.white);
        keynum7.setHorizontalAlignment(SwingConstants.CENTER);
        keynum7.setText("7");
        keynum7.setFocusable(false);
        keynum7.setMaximumSize(new java.awt.Dimension(40, 40));
        keynum7.setMinimumSize(new java.awt.Dimension(40, 40));
        keynum7.setName("0x67"); // NOI18N
        keynum7.setOpaque(true);
        keynum7.setPreferredSize(new java.awt.Dimension(40, 40));
        keynum7.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keynum8.setBackground(DEFAULT_KEY_COLOR);
        keynum8.setForeground(java.awt.Color.white);
        keynum8.setHorizontalAlignment(SwingConstants.CENTER);
        keynum8.setText("8");
        keynum8.setFocusable(false);
        keynum8.setMaximumSize(new java.awt.Dimension(40, 40));
        keynum8.setMinimumSize(new java.awt.Dimension(40, 40));
        keynum8.setName("0x68"); // NOI18N
        keynum8.setOpaque(true);
        keynum8.setPreferredSize(new java.awt.Dimension(40, 40));
        keynum8.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keynum9.setBackground(DEFAULT_KEY_COLOR);
        keynum9.setForeground(java.awt.Color.white);
        keynum9.setHorizontalAlignment(SwingConstants.CENTER);
        keynum9.setText("9");
        keynum9.setFocusable(false);
        keynum9.setMaximumSize(new java.awt.Dimension(40, 40));
        keynum9.setMinimumSize(new java.awt.Dimension(40, 40));
        keynum9.setName("0x69"); // NOI18N
        keynum9.setOpaque(true);
        keynum9.setPreferredSize(new java.awt.Dimension(40, 40));
        keynum9.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keynumplus.setBackground(DEFAULT_KEY_COLOR);
        keynumplus.setForeground(java.awt.Color.white);
        keynumplus.setHorizontalAlignment(SwingConstants.CENTER);
        keynumplus.setText("+");
        keynumplus.setFocusable(false);
        keynumplus.setMaximumSize(new java.awt.Dimension(40, 40));
        keynumplus.setMinimumSize(new java.awt.Dimension(40, 40));
        keynumplus.setName("0x6B"); // NOI18N
        keynumplus.setOpaque(true);
        keynumplus.setPreferredSize(new java.awt.Dimension(40, 40));
        keynumplus.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keynum5.setBackground(DEFAULT_KEY_COLOR);
        keynum5.setForeground(java.awt.Color.white);
        keynum5.setHorizontalAlignment(SwingConstants.CENTER);
        keynum5.setText("5");
        keynum5.setFocusable(false);
        keynum5.setMaximumSize(new java.awt.Dimension(40, 40));
        keynum5.setMinimumSize(new java.awt.Dimension(40, 40));
        keynum5.setName("0x65"); // NOI18N
        keynum5.setOpaque(true);
        keynum5.setPreferredSize(new java.awt.Dimension(40, 40));
        keynum5.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keynum6.setBackground(DEFAULT_KEY_COLOR);
        keynum6.setForeground(java.awt.Color.white);
        keynum6.setHorizontalAlignment(SwingConstants.CENTER);
        keynum6.setText("6");
        keynum6.setFocusable(false);
        keynum6.setMaximumSize(new java.awt.Dimension(40, 40));
        keynum6.setMinimumSize(new java.awt.Dimension(40, 40));
        keynum6.setName("0x66"); // NOI18N
        keynum6.setOpaque(true);
        keynum6.setPreferredSize(new java.awt.Dimension(40, 40));
        keynum6.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keynum4.setBackground(DEFAULT_KEY_COLOR);
        keynum4.setForeground(java.awt.Color.white);
        keynum4.setHorizontalAlignment(SwingConstants.CENTER);
        keynum4.setText("4");
        keynum4.setFocusable(false);
        keynum4.setMaximumSize(new java.awt.Dimension(40, 40));
        keynum4.setMinimumSize(new java.awt.Dimension(40, 40));
        keynum4.setName("0x64"); // NOI18N
        keynum4.setOpaque(true);
        keynum4.setPreferredSize(new java.awt.Dimension(40, 40));
        keynum4.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keynum3.setBackground(DEFAULT_KEY_COLOR);
        keynum3.setForeground(java.awt.Color.white);
        keynum3.setHorizontalAlignment(SwingConstants.CENTER);
        keynum3.setText("3");
        keynum3.setFocusable(false);
        keynum3.setMaximumSize(new java.awt.Dimension(40, 40));
        keynum3.setMinimumSize(new java.awt.Dimension(40, 40));
        keynum3.setName("0x63"); // NOI18N
        keynum3.setOpaque(true);
        keynum3.setPreferredSize(new java.awt.Dimension(40, 40));
        keynum3.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keynum2.setBackground(DEFAULT_KEY_COLOR);
        keynum2.setForeground(java.awt.Color.white);
        keynum2.setHorizontalAlignment(SwingConstants.CENTER);
        keynum2.setText("2");
        keynum2.setFocusable(false);
        keynum2.setMaximumSize(new java.awt.Dimension(40, 40));
        keynum2.setMinimumSize(new java.awt.Dimension(40, 40));
        keynum2.setName("0x62"); // NOI18N
        keynum2.setOpaque(true);
        keynum2.setPreferredSize(new java.awt.Dimension(40, 40));
        keynum2.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keynum1.setBackground(DEFAULT_KEY_COLOR);
        keynum1.setForeground(java.awt.Color.white);
        keynum1.setHorizontalAlignment(SwingConstants.CENTER);
        keynum1.setText("1");
        keynum1.setFocusable(false);
        keynum1.setMaximumSize(new java.awt.Dimension(40, 40));
        keynum1.setMinimumSize(new java.awt.Dimension(40, 40));
        keynum1.setName("0x61"); // NOI18N
        keynum1.setOpaque(true);
        keynum1.setPreferredSize(new java.awt.Dimension(40, 40));
        keynum1.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keynumenter.setBackground(DEFAULT_KEY_COLOR);
        keynumenter.setForeground(java.awt.Color.white);
        keynumenter.setHorizontalAlignment(SwingConstants.CENTER);
        keynumenter.setText("Enter");
        keynumenter.setFocusable(false);
        keynumenter.setMaximumSize(new java.awt.Dimension(40, 86));
        keynumenter.setMinimumSize(new java.awt.Dimension(40, 86));
        keynumenter.setName("0x0A"); // NOI18N
        keynumenter.setOpaque(true);
        keynumenter.setPreferredSize(new java.awt.Dimension(40, 86));
        keynumenter.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keynumdot.setBackground(DEFAULT_KEY_COLOR);
        keynumdot.setForeground(java.awt.Color.white);
        keynumdot.setHorizontalAlignment(SwingConstants.CENTER);
        keynumdot.setText(".");
        keynumdot.setFocusable(false);
        keynumdot.setMaximumSize(new java.awt.Dimension(40, 40));
        keynumdot.setMinimumSize(new java.awt.Dimension(40, 40));
        keynumdot.setName("0x6E"); // NOI18N
        keynumdot.setOpaque(true);
        keynumdot.setPreferredSize(new java.awt.Dimension(40, 40));
        keynumdot.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keynum0.setBackground(DEFAULT_KEY_COLOR);
        keynum0.setForeground(java.awt.Color.white);
        keynum0.setHorizontalAlignment(SwingConstants.CENTER);
        keynum0.setText("0");
        keynum0.setFocusable(false);
        keynum0.setMaximumSize(new java.awt.Dimension(86, 40));
        keynum0.setMinimumSize(new java.awt.Dimension(86, 40));
        keynum0.setName("0x60"); // NOI18N
        keynum0.setOpaque(true);
        keynum0.setPreferredSize(new java.awt.Dimension(86, 40));
        keynum0.addMouseListener(new java.awt.event.MouseAdapter() {
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

//        GroupLayout numpadPanelLayout = new GroupLayout(numpadPanel);
//        numpadPanel.setLayout(numpadPanelLayout);
//        numpadPanelLayout.setHorizontalGroup(
//            numpadPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//            .addGroup(numpadPanelLayout.createSequentialGroup()
//                .addGroup(numpadPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
//                    .addGroup(numpadPanelLayout.createSequentialGroup()
//                        .addComponent(keynum1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                        .addGap(4, 4, 4)
//                        .addComponent(keynum2, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
//                    .addComponent(keynum0, GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
//                .addGap(4, 4, 4)
//                .addGroup(numpadPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                    .addComponent(keynum3, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                    .addComponent(keynumdot, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
//                .addGap(4, 4, 4)
//                .addComponent(keynumenter, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
//            .addGroup(numpadPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
//                .addGroup(numpadPanelLayout.createSequentialGroup()
//                    .addComponent(keyprtscr, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                    .addGap(4, 4, 4)
//                    .addComponent(keynumdivide, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                    .addGap(4, 4, 4)
//                    .addComponent(keynummult, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                    .addGap(4, 4, 4)
//                    .addComponent(keynumminus, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
//                .addGroup(numpadPanelLayout.createSequentialGroup()
//                    .addGroup(numpadPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                        .addGroup(numpadPanelLayout.createSequentialGroup()
//                            .addComponent(keynum7, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                            .addGap(4, 4, 4)
//                            .addComponent(keynum8, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                            .addGap(4, 4, 4)
//                            .addComponent(keynum9, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
//                        .addGroup(numpadPanelLayout.createSequentialGroup()
//                            .addComponent(keynum4, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                            .addGap(4, 4, 4)
//                            .addComponent(keynum5, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                            .addGap(4, 4, 4)
//                            .addComponent(keynum6, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)))
//                    .addGap(4, 4, 4)
//                    .addComponent(keynumplus, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
//        );
//        numpadPanelLayout.setVerticalGroup(
//            numpadPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//            .addGroup(numpadPanelLayout.createSequentialGroup()
//                .addGroup(numpadPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                    .addComponent(keyprtscr, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                    .addComponent(keynumdivide, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                    .addGroup(numpadPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                        .addComponent(keynumminus, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                        .addComponent(keynummult, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)))
//                .addGap(4, 4, 4)
//                .addGroup(numpadPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
//                    .addGroup(numpadPanelLayout.createSequentialGroup()
//                        .addGroup(numpadPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                            .addComponent(keynum7, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                            .addComponent(keynum8, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                            .addComponent(keynum9, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
//                        .addGap(4, 4, 4)
//                        .addGroup(numpadPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                            .addComponent(keynum4, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                            .addComponent(keynum5, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                            .addComponent(keynum6, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)))
//                    .addComponent(keynumplus, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//                .addGap(4, 4, 4)
//                .addGroup(numpadPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                    .addGroup(numpadPanelLayout.createSequentialGroup()
//                        .addGroup(numpadPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                            .addComponent(keynum1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                            .addComponent(keynum2, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                            .addComponent(keynum3, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
//                        .addGap(4, 4, 4)
//                        .addGroup(numpadPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
//                            .addComponent(keynumdot, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                            .addComponent(keynum0, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)))
//                    .addComponent(keynumenter, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE))
//                .addGap(0, 0, 0))
//        );

        functionPanel.setBackground(java.awt.Color.black);
        functionPanel.setFocusable(false);

        keybackquote.setBackground(DEFAULT_KEY_COLOR);
        keybackquote.setForeground(java.awt.Color.white);
        keybackquote.setHorizontalAlignment(SwingConstants.CENTER);
        keybackquote.setText("<html>~<br />&nbsp;&nbsp;`</html>");
        keybackquote.setFocusable(false);
        keybackquote.setMaximumSize(new java.awt.Dimension(40, 40));
        keybackquote.setMinimumSize(new java.awt.Dimension(40, 40));
        keybackquote.setName("0xC0"); // NOI18N
        keybackquote.setOpaque(true);
        keybackquote.setPreferredSize(new java.awt.Dimension(40, 40));
        keybackquote.addMouseListener(new java.awt.event.MouseAdapter() {
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

        key1.setBackground(DEFAULT_KEY_COLOR);
        key1.setForeground(java.awt.Color.white);
        key1.setHorizontalAlignment(SwingConstants.CENTER);
        key1.setText("<html><br />&nbsp;&nbsp;1</html>");
        key1.setFocusable(false);
        key1.setMaximumSize(new java.awt.Dimension(40, 40));
        key1.setMinimumSize(new java.awt.Dimension(40, 40));
        key1.setName("0x31"); // NOI18N
        key1.setOpaque(true);
        key1.setPreferredSize(new java.awt.Dimension(40, 40));
        key1.addMouseListener(new java.awt.event.MouseAdapter() {
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

        key2.setBackground(DEFAULT_KEY_COLOR);
        key2.setForeground(java.awt.Color.white);
        key2.setHorizontalAlignment(SwingConstants.CENTER);
        key2.setText("<html><br />&nbsp;&nbsp;2</html>");
        key2.setFocusable(false);
        key2.setMaximumSize(new java.awt.Dimension(40, 40));
        key2.setMinimumSize(new java.awt.Dimension(40, 40));
        key2.setName("0x32"); // NOI18N
        key2.setOpaque(true);
        key2.setPreferredSize(new java.awt.Dimension(40, 40));
        key2.addMouseListener(new java.awt.event.MouseAdapter() {
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

        key3.setBackground(DEFAULT_KEY_COLOR);
        key3.setForeground(java.awt.Color.white);
        key3.setHorizontalAlignment(SwingConstants.CENTER);
        key3.setText("<html><br />&nbsp;&nbsp;3</html>");
        key3.setFocusable(false);
        key3.setMaximumSize(new java.awt.Dimension(40, 40));
        key3.setMinimumSize(new java.awt.Dimension(40, 40));
        key3.setName("0x33"); // NOI18N
        key3.setOpaque(true);
        key3.setPreferredSize(new java.awt.Dimension(40, 40));
        key3.addMouseListener(new java.awt.event.MouseAdapter() {
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

        key4.setBackground(DEFAULT_KEY_COLOR);
        key4.setForeground(java.awt.Color.white);
        key4.setHorizontalAlignment(SwingConstants.CENTER);
        key4.setText("<html><br />&nbsp;&nbsp;4</html>");
        key4.setFocusable(false);
        key4.setMaximumSize(new java.awt.Dimension(40, 40));
        key4.setMinimumSize(new java.awt.Dimension(40, 40));
        key4.setName("0x34"); // NOI18N
        key4.setOpaque(true);
        key4.setPreferredSize(new java.awt.Dimension(40, 40));
        key4.addMouseListener(new java.awt.event.MouseAdapter() {
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

        key5.setBackground(DEFAULT_KEY_COLOR);
        key5.setForeground(java.awt.Color.white);
        key5.setHorizontalAlignment(SwingConstants.CENTER);
        key5.setText("<html><br />&nbsp;&nbsp;5</html>");
        key5.setFocusable(false);
        key5.setMaximumSize(new java.awt.Dimension(40, 40));
        key5.setMinimumSize(new java.awt.Dimension(40, 40));
        key5.setName("0x35"); // NOI18N
        key5.setOpaque(true);
        key5.setPreferredSize(new java.awt.Dimension(40, 40));
        key5.addMouseListener(new java.awt.event.MouseAdapter() {
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

        key6.setBackground(DEFAULT_KEY_COLOR);
        key6.setForeground(java.awt.Color.white);
        key6.setHorizontalAlignment(SwingConstants.CENTER);
        key6.setText("<html><br />&nbsp;&nbsp;6</html>");
        key6.setFocusable(false);
        key6.setMaximumSize(new java.awt.Dimension(40, 40));
        key6.setMinimumSize(new java.awt.Dimension(40, 40));
        key6.setName("0x36"); // NOI18N
        key6.setOpaque(true);
        key6.setPreferredSize(new java.awt.Dimension(40, 40));
        key6.addMouseListener(new java.awt.event.MouseAdapter() {
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

        key7.setBackground(DEFAULT_KEY_COLOR);
        key7.setForeground(java.awt.Color.white);
        key7.setHorizontalAlignment(SwingConstants.CENTER);
        key7.setText("<html><br />&nbsp;&nbsp;7</html>");
        key7.setFocusable(false);
        key7.setMaximumSize(new java.awt.Dimension(40, 40));
        key7.setMinimumSize(new java.awt.Dimension(40, 40));
        key7.setName("0x37"); // NOI18N
        key7.setOpaque(true);
        key7.setPreferredSize(new java.awt.Dimension(40, 40));
        key7.addMouseListener(new java.awt.event.MouseAdapter() {
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

        key8.setBackground(DEFAULT_KEY_COLOR);
        key8.setForeground(java.awt.Color.white);
        key8.setHorizontalAlignment(SwingConstants.CENTER);
        key8.setText("<html><br />&nbsp;&nbsp;8</html>");
        key8.setFocusable(false);
        key8.setMaximumSize(new java.awt.Dimension(40, 40));
        key8.setMinimumSize(new java.awt.Dimension(40, 40));
        key8.setName("0x38"); // NOI18N
        key8.setOpaque(true);
        key8.setPreferredSize(new java.awt.Dimension(40, 40));
        key8.addMouseListener(new java.awt.event.MouseAdapter() {
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

        key9.setBackground(DEFAULT_KEY_COLOR);
        key9.setForeground(java.awt.Color.white);
        key9.setHorizontalAlignment(SwingConstants.CENTER);
        key9.setText("<html><br />&nbsp;&nbsp;9</html>");
        key9.setFocusable(false);
        key9.setMaximumSize(new java.awt.Dimension(40, 40));
        key9.setMinimumSize(new java.awt.Dimension(40, 40));
        key9.setName("0x39"); // NOI18N
        key9.setOpaque(true);
        key9.setPreferredSize(new java.awt.Dimension(40, 40));
        key9.addMouseListener(new java.awt.event.MouseAdapter() {
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

        key0.setBackground(DEFAULT_KEY_COLOR);
        key0.setForeground(java.awt.Color.white);
        key0.setHorizontalAlignment(SwingConstants.CENTER);
        key0.setText("<html><br />&nbsp;&nbsp;0</html>");
        key0.setFocusable(false);
        key0.setMaximumSize(new java.awt.Dimension(40, 40));
        key0.setMinimumSize(new java.awt.Dimension(40, 40));
        key0.setName("0x30"); // NOI18N
        key0.setOpaque(true);
        key0.setPreferredSize(new java.awt.Dimension(40, 40));
        key0.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyminus.setBackground(DEFAULT_KEY_COLOR);
        keyminus.setForeground(java.awt.Color.white);
        keyminus.setHorizontalAlignment(SwingConstants.CENTER);
        keyminus.setText("<html>_<br />&nbsp;&nbsp;-</html>");
        keyminus.setFocusable(false);
        keyminus.setMaximumSize(new java.awt.Dimension(40, 40));
        keyminus.setMinimumSize(new java.awt.Dimension(40, 40));
        keyminus.setName("0x2D"); // NOI18N
        keyminus.setOpaque(true);
        keyminus.setPreferredSize(new java.awt.Dimension(40, 40));
        keyminus.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyequal.setBackground(DEFAULT_KEY_COLOR);
        keyequal.setForeground(java.awt.Color.white);
        keyequal.setHorizontalAlignment(SwingConstants.CENTER);
        keyequal.setText("<html>+<br />&nbsp;&nbsp;=</html>");
        keyequal.setFocusable(false);
        keyequal.setMaximumSize(new java.awt.Dimension(40, 40));
        keyequal.setMinimumSize(new java.awt.Dimension(40, 40));
        keyequal.setName("0x3D"); // NOI18N
        keyequal.setOpaque(true);
        keyequal.setPreferredSize(new java.awt.Dimension(40, 40));
        keyequal.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keybackspace.setBackground(DEFAULT_KEY_COLOR);
        keybackspace.setForeground(java.awt.Color.white);
        keybackspace.setHorizontalAlignment(SwingConstants.CENTER);
        keybackspace.setText("Backspace");
        keybackspace.setFocusable(false);
        keybackspace.setMaximumSize(new java.awt.Dimension(60, 40));
        keybackspace.setMinimumSize(new java.awt.Dimension(60, 40));
        keybackspace.setName("0x08"); // NOI18N
        keybackspace.setOpaque(true);
        keybackspace.setPreferredSize(new java.awt.Dimension(60, 40));
        keybackspace.addMouseListener(new java.awt.event.MouseAdapter() {
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

        alphabetPanel.setBackground(java.awt.Color.black);
        alphabetPanel.setFocusable(false);

        keytab.setBackground(DEFAULT_KEY_COLOR);
        keytab.setForeground(java.awt.Color.white);
        keytab.setHorizontalAlignment(SwingConstants.LEFT);
        keytab.setText(" Tab");
        keytab.setFocusable(false);
        keytab.setMaximumSize(new java.awt.Dimension(60, 40));
        keytab.setMinimumSize(new java.awt.Dimension(60, 40));
        keytab.setName("0x09"); // NOI18N
        keytab.setOpaque(true);
        keytab.setPreferredSize(new java.awt.Dimension(60, 40));
        keytab.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyq.setBackground(DEFAULT_KEY_COLOR);
        keyq.setForeground(java.awt.Color.white);
        keyq.setHorizontalAlignment(SwingConstants.CENTER);
        keyq.setText("q");
        keyq.setFocusable(false);
        keyq.setMaximumSize(new java.awt.Dimension(40, 40));
        keyq.setMinimumSize(new java.awt.Dimension(40, 40));
        keyq.setName("0x51"); // NOI18N
        keyq.setOpaque(true);
        keyq.setPreferredSize(new java.awt.Dimension(40, 40));
        keyq.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyw.setBackground(DEFAULT_KEY_COLOR);
        keyw.setForeground(java.awt.Color.white);
        keyw.setHorizontalAlignment(SwingConstants.CENTER);
        keyw.setText("w");
        keyw.setFocusable(false);
        keyw.setMaximumSize(new java.awt.Dimension(40, 40));
        keyw.setMinimumSize(new java.awt.Dimension(40, 40));
        keyw.setName("0x57"); // NOI18N
        keyw.setOpaque(true);
        keyw.setPreferredSize(new java.awt.Dimension(40, 40));
        keyw.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keye.setBackground(DEFAULT_KEY_COLOR);
        keye.setForeground(java.awt.Color.white);
        keye.setHorizontalAlignment(SwingConstants.CENTER);
        keye.setText("e");
        keye.setFocusable(false);
        keye.setMaximumSize(new java.awt.Dimension(40, 40));
        keye.setMinimumSize(new java.awt.Dimension(40, 40));
        keye.setName("0x45"); // NOI18N
        keye.setOpaque(true);
        keye.setPreferredSize(new java.awt.Dimension(40, 40));
        keye.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyr.setBackground(DEFAULT_KEY_COLOR);
        keyr.setForeground(java.awt.Color.white);
        keyr.setHorizontalAlignment(SwingConstants.CENTER);
        keyr.setText("r");
        keyr.setFocusable(false);
        keyr.setMaximumSize(new java.awt.Dimension(40, 40));
        keyr.setMinimumSize(new java.awt.Dimension(40, 40));
        keyr.setName("0x52"); // NOI18N
        keyr.setOpaque(true);
        keyr.setPreferredSize(new java.awt.Dimension(40, 40));
        keyr.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyt.setBackground(DEFAULT_KEY_COLOR);
        keyt.setForeground(java.awt.Color.white);
        keyt.setHorizontalAlignment(SwingConstants.CENTER);
        keyt.setText("t");
        keyt.setFocusable(false);
        keyt.setMaximumSize(new java.awt.Dimension(40, 40));
        keyt.setMinimumSize(new java.awt.Dimension(40, 40));
        keyt.setName("0x54"); // NOI18N
        keyt.setOpaque(true);
        keyt.setPreferredSize(new java.awt.Dimension(40, 40));
        keyt.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyy.setBackground(DEFAULT_KEY_COLOR);
        keyy.setForeground(java.awt.Color.white);
        keyy.setHorizontalAlignment(SwingConstants.CENTER);
        keyy.setText("y");
        keyy.setFocusable(false);
        keyy.setMaximumSize(new java.awt.Dimension(40, 40));
        keyy.setMinimumSize(new java.awt.Dimension(40, 40));
        keyy.setName("0x59"); // NOI18N
        keyy.setOpaque(true);
        keyy.setPreferredSize(new java.awt.Dimension(40, 40));
        keyy.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyu.setBackground(DEFAULT_KEY_COLOR);
        keyu.setForeground(java.awt.Color.white);
        keyu.setHorizontalAlignment(SwingConstants.CENTER);
        keyu.setText("u");
        keyu.setFocusable(false);
        keyu.setMaximumSize(new java.awt.Dimension(40, 40));
        keyu.setMinimumSize(new java.awt.Dimension(40, 40));
        keyu.setName("0x55"); // NOI18N
        keyu.setOpaque(true);
        keyu.setPreferredSize(new java.awt.Dimension(40, 40));
        keyu.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyi.setBackground(DEFAULT_KEY_COLOR);
        keyi.setForeground(java.awt.Color.white);
        keyi.setHorizontalAlignment(SwingConstants.CENTER);
        keyi.setText("i");
        keyi.setFocusable(false);
        keyi.setMaximumSize(new java.awt.Dimension(40, 40));
        keyi.setMinimumSize(new java.awt.Dimension(40, 40));
        keyi.setName("0x49"); // NOI18N
        keyi.setOpaque(true);
        keyi.setPreferredSize(new java.awt.Dimension(40, 40));
        keyi.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyo.setBackground(DEFAULT_KEY_COLOR);
        keyo.setForeground(java.awt.Color.white);
        keyo.setHorizontalAlignment(SwingConstants.CENTER);
        keyo.setText("o");
        keyo.setFocusable(false);
        keyo.setMaximumSize(new java.awt.Dimension(40, 40));
        keyo.setMinimumSize(new java.awt.Dimension(40, 40));
        keyo.setName("0x4F"); // NOI18N
        keyo.setOpaque(true);
        keyo.setPreferredSize(new java.awt.Dimension(40, 40));
        keyo.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyp.setBackground(DEFAULT_KEY_COLOR);
        keyp.setForeground(java.awt.Color.white);
        keyp.setHorizontalAlignment(SwingConstants.CENTER);
        keyp.setText("p");
        keyp.setFocusable(false);
        keyp.setMaximumSize(new java.awt.Dimension(40, 40));
        keyp.setMinimumSize(new java.awt.Dimension(40, 40));
        keyp.setName("0x50"); // NOI18N
        keyp.setOpaque(true);
        keyp.setPreferredSize(new java.awt.Dimension(40, 40));
        keyp.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyopenbigbracket.setBackground(DEFAULT_KEY_COLOR);
        keyopenbigbracket.setForeground(java.awt.Color.white);
        keyopenbigbracket.setHorizontalAlignment(SwingConstants.CENTER);
        keyopenbigbracket.setText("<html>{<br />&nbsp;&nbsp;[</html>");
        keyopenbigbracket.setFocusable(false);
        keyopenbigbracket.setMaximumSize(new java.awt.Dimension(40, 40));
        keyopenbigbracket.setMinimumSize(new java.awt.Dimension(40, 40));
        keyopenbigbracket.setName("0x5B"); // NOI18N
        keyopenbigbracket.setOpaque(true);
        keyopenbigbracket.setPreferredSize(new java.awt.Dimension(40, 40));
        keyopenbigbracket.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyclosebigbracket.setBackground(DEFAULT_KEY_COLOR);
        keyclosebigbracket.setForeground(java.awt.Color.white);
        keyclosebigbracket.setHorizontalAlignment(SwingConstants.CENTER);
        keyclosebigbracket.setText("<html>}<br />&nbsp;&nbsp;]</html>");
        keyclosebigbracket.setFocusable(false);
        keyclosebigbracket.setMaximumSize(new java.awt.Dimension(40, 40));
        keyclosebigbracket.setMinimumSize(new java.awt.Dimension(40, 40));
        keyclosebigbracket.setName("0x5D"); // NOI18N
        keyclosebigbracket.setOpaque(true);
        keyclosebigbracket.setPreferredSize(new java.awt.Dimension(40, 40));
        keyclosebigbracket.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keybslash.setBackground(DEFAULT_KEY_COLOR);
        keybslash.setForeground(java.awt.Color.white);
        keybslash.setHorizontalAlignment(SwingConstants.CENTER);
        keybslash.setText("<html>|<br />&nbsp;&nbsp;\\</html>");
        keybslash.setFocusable(false);
        keybslash.setMaximumSize(new java.awt.Dimension(40, 40));
        keybslash.setMinimumSize(new java.awt.Dimension(40, 40));
        keybslash.setName("0x5C"); // NOI18N
        keybslash.setOpaque(true);
        keybslash.setPreferredSize(new java.awt.Dimension(40, 40));
        keybslash.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyj.setBackground(DEFAULT_KEY_COLOR);
        keyj.setForeground(java.awt.Color.white);
        keyj.setHorizontalAlignment(SwingConstants.CENTER);
        keyj.setText("j");
        keyj.setFocusable(false);
        keyj.setMaximumSize(new java.awt.Dimension(40, 40));
        keyj.setMinimumSize(new java.awt.Dimension(40, 40));
        keyj.setName("0x4A"); // NOI18N
        keyj.setOpaque(true);
        keyj.setPreferredSize(new java.awt.Dimension(40, 40));
        keyj.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyk.setBackground(DEFAULT_KEY_COLOR);
        keyk.setForeground(java.awt.Color.white);
        keyk.setHorizontalAlignment(SwingConstants.CENTER);
        keyk.setText("k");
        keyk.setFocusable(false);
        keyk.setMaximumSize(new java.awt.Dimension(40, 40));
        keyk.setMinimumSize(new java.awt.Dimension(40, 40));
        keyk.setName("0x4B"); // NOI18N
        keyk.setOpaque(true);
        keyk.setPreferredSize(new java.awt.Dimension(40, 40));
        keyk.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyl.setBackground(DEFAULT_KEY_COLOR);
        keyl.setForeground(java.awt.Color.white);
        keyl.setHorizontalAlignment(SwingConstants.CENTER);
        keyl.setText("l");
        keyl.setFocusable(false);
        keyl.setMaximumSize(new java.awt.Dimension(40, 40));
        keyl.setMinimumSize(new java.awt.Dimension(40, 40));
        keyl.setName("0x4C"); // NOI18N
        keyl.setOpaque(true);
        keyl.setPreferredSize(new java.awt.Dimension(40, 40));
        keyl.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keycaps.setBackground(DEFAULT_KEY_COLOR);
        keycaps.setForeground(java.awt.Color.white);
        keycaps.setHorizontalAlignment(SwingConstants.LEFT);
        keycaps.setText(" CapsLock");
        keycaps.setFocusable(false);
        keycaps.setMaximumSize(new java.awt.Dimension(70, 40));
        keycaps.setMinimumSize(new java.awt.Dimension(70, 40));
        keycaps.setName("0x14"); // NOI18N
        keycaps.setOpaque(true);
        keycaps.setPreferredSize(new java.awt.Dimension(70, 40));
        keycaps.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keysemicolon.setBackground(DEFAULT_KEY_COLOR);
        keysemicolon.setForeground(java.awt.Color.white);
        keysemicolon.setHorizontalAlignment(SwingConstants.CENTER);
        keysemicolon.setText("<html>:<br />&nbsp;&nbsp;;</html>");
        keysemicolon.setFocusable(false);
        keysemicolon.setMaximumSize(new java.awt.Dimension(40, 40));
        keysemicolon.setMinimumSize(new java.awt.Dimension(40, 40));
        keysemicolon.setName("0x3B"); // NOI18N
        keysemicolon.setOpaque(true);
        keysemicolon.setPreferredSize(new java.awt.Dimension(40, 40));
        keysemicolon.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyquote.setBackground(DEFAULT_KEY_COLOR);
        keyquote.setForeground(java.awt.Color.white);
        keyquote.setHorizontalAlignment(SwingConstants.CENTER);
        keyquote.setText("<html>\"<br />&nbsp;&nbsp;'</html>");
        keyquote.setFocusable(false);
        keyquote.setMaximumSize(new java.awt.Dimension(40, 40));
        keyquote.setMinimumSize(new java.awt.Dimension(40, 40));
        keyquote.setName("0xDE"); // NOI18N
        keyquote.setOpaque(true);
        keyquote.setPreferredSize(new java.awt.Dimension(40, 40));
        keyquote.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keya.setBackground(DEFAULT_KEY_COLOR);
        keya.setForeground(java.awt.Color.white);
        keya.setHorizontalAlignment(SwingConstants.CENTER);
        keya.setText("a");
        keya.setFocusable(false);
        keya.setMaximumSize(new java.awt.Dimension(40, 40));
        keya.setMinimumSize(new java.awt.Dimension(40, 40));
        keya.setName("0x41"); // NOI18N
        keya.setOpaque(true);
        keya.setPreferredSize(new java.awt.Dimension(40, 40));
        keya.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyenter.setBackground(DEFAULT_KEY_COLOR);
        keyenter.setForeground(java.awt.Color.white);
        keyenter.setHorizontalAlignment(SwingConstants.RIGHT);
        keyenter.setText("Enter ");
        keyenter.setFocusable(false);
        keyenter.setMaximumSize(new java.awt.Dimension(76, 40));
        keyenter.setMinimumSize(new java.awt.Dimension(76, 40));
        keyenter.setName("0x0A"); // NOI18N
        keyenter.setOpaque(true);
        keyenter.setPreferredSize(new java.awt.Dimension(76, 40));
        keyenter.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keys.setBackground(DEFAULT_KEY_COLOR);
        keys.setForeground(java.awt.Color.white);
        keys.setHorizontalAlignment(SwingConstants.CENTER);
        keys.setText("s");
        keys.setFocusable(false);
        keys.setMaximumSize(new java.awt.Dimension(40, 40));
        keys.setMinimumSize(new java.awt.Dimension(40, 40));
        keys.setName("0x53"); // NOI18N
        keys.setOpaque(true);
        keys.setPreferredSize(new java.awt.Dimension(40, 40));
        keys.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyd.setBackground(DEFAULT_KEY_COLOR);
        keyd.setForeground(java.awt.Color.white);
        keyd.setHorizontalAlignment(SwingConstants.CENTER);
        keyd.setText("d");
        keyd.setFocusable(false);
        keyd.setMaximumSize(new java.awt.Dimension(40, 40));
        keyd.setMinimumSize(new java.awt.Dimension(40, 40));
        keyd.setName("0x44"); // NOI18N
        keyd.setOpaque(true);
        keyd.setPreferredSize(new java.awt.Dimension(40, 40));
        keyd.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyf.setBackground(DEFAULT_KEY_COLOR);
        keyf.setForeground(java.awt.Color.white);
        keyf.setHorizontalAlignment(SwingConstants.CENTER);
        keyf.setText("f");
        keyf.setFocusable(false);
        keyf.setMaximumSize(new java.awt.Dimension(40, 40));
        keyf.setMinimumSize(new java.awt.Dimension(40, 40));
        keyf.setName("0x46"); // NOI18N
        keyf.setOpaque(true);
        keyf.setPreferredSize(new java.awt.Dimension(40, 40));
        keyf.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyg.setBackground(DEFAULT_KEY_COLOR);
        keyg.setForeground(java.awt.Color.white);
        keyg.setHorizontalAlignment(SwingConstants.CENTER);
        keyg.setText("g");
        keyg.setFocusable(false);
        keyg.setMaximumSize(new java.awt.Dimension(40, 40));
        keyg.setMinimumSize(new java.awt.Dimension(40, 40));
        keyg.setName("0x47"); // NOI18N
        keyg.setOpaque(true);
        keyg.setPreferredSize(new java.awt.Dimension(40, 40));
        keyg.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyh.setBackground(DEFAULT_KEY_COLOR);
        keyh.setForeground(java.awt.Color.white);
        keyh.setHorizontalAlignment(SwingConstants.CENTER);
        keyh.setText("h");
        keyh.setFocusable(false);
        keyh.setMaximumSize(new java.awt.Dimension(40, 40));
        keyh.setMinimumSize(new java.awt.Dimension(40, 40));
        keyh.setName("0x48"); // NOI18N
        keyh.setOpaque(true);
        keyh.setPreferredSize(new java.awt.Dimension(40, 40));
        keyh.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keym.setBackground(DEFAULT_KEY_COLOR);
        keym.setForeground(java.awt.Color.white);
        keym.setHorizontalAlignment(SwingConstants.CENTER);
        keym.setText("m");
        keym.setFocusable(false);
        keym.setMaximumSize(new java.awt.Dimension(40, 40));
        keym.setMinimumSize(new java.awt.Dimension(40, 40));
        keym.setName("0x4D"); // NOI18N
        keym.setOpaque(true);
        keym.setPreferredSize(new java.awt.Dimension(40, 40));
        keym.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyshiftl.setBackground(DEFAULT_KEY_COLOR);
        keyshiftl.setForeground(java.awt.Color.white);
        keyshiftl.setHorizontalAlignment(SwingConstants.LEFT);
        keyshiftl.setText(" Shift");
        keyshiftl.setFocusable(false);
        keyshiftl.setMaximumSize(new java.awt.Dimension(87, 40));
        keyshiftl.setMinimumSize(new java.awt.Dimension(87, 40));
        keyshiftl.setName("0x10"); // NOI18N
        keyshiftl.setOpaque(true);
        keyshiftl.setPreferredSize(new java.awt.Dimension(87, 40));
        keyshiftl.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyfslash.setBackground(DEFAULT_KEY_COLOR);
        keyfslash.setForeground(java.awt.Color.white);
        keyfslash.setHorizontalAlignment(SwingConstants.CENTER);
        keyfslash.setText("<html>?<br />&nbsp;&nbsp;/</html>");
        keyfslash.setFocusable(false);
        keyfslash.setMaximumSize(new java.awt.Dimension(40, 40));
        keyfslash.setMinimumSize(new java.awt.Dimension(40, 40));
        keyfslash.setName("0x2F"); // NOI18N
        keyfslash.setOpaque(true);
        keyfslash.setPreferredSize(new java.awt.Dimension(40, 40));
        keyfslash.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyz.setBackground(DEFAULT_KEY_COLOR);
        keyz.setForeground(java.awt.Color.white);
        keyz.setHorizontalAlignment(SwingConstants.CENTER);
        keyz.setText("z");
        keyz.setFocusable(false);
        keyz.setMaximumSize(new java.awt.Dimension(40, 40));
        keyz.setMinimumSize(new java.awt.Dimension(40, 40));
        keyz.setName("0x5A"); // NOI18N
        keyz.setOpaque(true);
        keyz.setPreferredSize(new java.awt.Dimension(40, 40));
        keyz.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyshiftr.setBackground(DEFAULT_KEY_COLOR);
        keyshiftr.setForeground(java.awt.Color.white);
        keyshiftr.setHorizontalAlignment(SwingConstants.RIGHT);
        keyshiftr.setText("Shift ");
        keyshiftr.setFocusable(false);
        keyshiftr.setMaximumSize(new java.awt.Dimension(105, 40));
        keyshiftr.setMinimumSize(new java.awt.Dimension(105, 40));
        keyshiftr.setName("0x10"); // NOI18N
        keyshiftr.setOpaque(true);
        keyshiftr.setPreferredSize(new java.awt.Dimension(105, 40));
        keyshiftr.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyx.setBackground(DEFAULT_KEY_COLOR);
        keyx.setForeground(java.awt.Color.white);
        keyx.setHorizontalAlignment(SwingConstants.CENTER);
        keyx.setText("x");
        keyx.setFocusable(false);
        keyx.setMaximumSize(new java.awt.Dimension(40, 40));
        keyx.setMinimumSize(new java.awt.Dimension(40, 40));
        keyx.setName("0x58"); // NOI18N
        keyx.setOpaque(true);
        keyx.setPreferredSize(new java.awt.Dimension(40, 40));
        keyx.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyc.setBackground(DEFAULT_KEY_COLOR);
        keyc.setForeground(java.awt.Color.white);
        keyc.setHorizontalAlignment(SwingConstants.CENTER);
        keyc.setText("c");
        keyc.setFocusable(false);
        keyc.setMaximumSize(new java.awt.Dimension(40, 40));
        keyc.setMinimumSize(new java.awt.Dimension(40, 40));
        keyc.setName("0x43"); // NOI18N
        keyc.setOpaque(true);
        keyc.setPreferredSize(new java.awt.Dimension(40, 40));
        keyc.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyv.setBackground(DEFAULT_KEY_COLOR);
        keyv.setForeground(java.awt.Color.white);
        keyv.setHorizontalAlignment(SwingConstants.CENTER);
        keyv.setText("v");
        keyv.setFocusable(false);
        keyv.setMaximumSize(new java.awt.Dimension(40, 40));
        keyv.setMinimumSize(new java.awt.Dimension(40, 40));
        keyv.setName("0x56"); // NOI18N
        keyv.setOpaque(true);
        keyv.setPreferredSize(new java.awt.Dimension(40, 40));
        keyv.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyb.setBackground(DEFAULT_KEY_COLOR);
        keyb.setForeground(java.awt.Color.white);
        keyb.setHorizontalAlignment(SwingConstants.CENTER);
        keyb.setText("b");
        keyb.setFocusable(false);
        keyb.setMaximumSize(new java.awt.Dimension(40, 40));
        keyb.setMinimumSize(new java.awt.Dimension(40, 40));
        keyb.setName("0x42"); // NOI18N
        keyb.setOpaque(true);
        keyb.setPreferredSize(new java.awt.Dimension(40, 40));
        keyb.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyn.setBackground(DEFAULT_KEY_COLOR);
        keyn.setForeground(java.awt.Color.white);
        keyn.setHorizontalAlignment(SwingConstants.CENTER);
        keyn.setText("n");
        keyn.setFocusable(false);
        keyn.setMaximumSize(new java.awt.Dimension(40, 40));
        keyn.setMinimumSize(new java.awt.Dimension(40, 40));
        keyn.setName("0x4E"); // NOI18N
        keyn.setOpaque(true);
        keyn.setPreferredSize(new java.awt.Dimension(40, 40));
        keyn.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyrightarrow.setBackground(DEFAULT_KEY_COLOR);
        keyrightarrow.setForeground(java.awt.Color.white);
        keyrightarrow.setHorizontalAlignment(SwingConstants.CENTER);
        keyrightarrow.setText(">");
        keyrightarrow.setFocusable(false);
        keyrightarrow.setMaximumSize(new java.awt.Dimension(40, 40));
        keyrightarrow.setMinimumSize(new java.awt.Dimension(40, 40));
        keyrightarrow.setName("0x27"); // NOI18N
        keyrightarrow.setOpaque(true);
        keyrightarrow.setPreferredSize(new java.awt.Dimension(40, 40));
        keyrightarrow.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyctrl1.setBackground(DEFAULT_KEY_COLOR);
        keyctrl1.setForeground(java.awt.Color.white);
        keyctrl1.setHorizontalAlignment(SwingConstants.CENTER);
        keyctrl1.setText("Ctrl");
        keyctrl1.setFocusable(false);
        keyctrl1.setMaximumSize(new java.awt.Dimension(40, 40));
        keyctrl1.setMinimumSize(new java.awt.Dimension(40, 40));
        keyctrl1.setName("0x11"); // NOI18N
        keyctrl1.setOpaque(true);
        keyctrl1.setPreferredSize(new java.awt.Dimension(40, 40));
        keyctrl1.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyfn.setBackground(DEFAULT_KEY_COLOR);
        keyfn.setForeground(java.awt.Color.white);
        keyfn.setHorizontalAlignment(SwingConstants.CENTER);
        keyfn.setText("Fn");
        keyfn.setFocusable(false);
        keyfn.setMaximumSize(new java.awt.Dimension(40, 40));
        keyfn.setMinimumSize(new java.awt.Dimension(40, 40));
        keyfn.setName("0xff"); // NOI18N
        keyfn.setOpaque(true);
        keyfn.setPreferredSize(new java.awt.Dimension(40, 40));
        keyfn.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keycommercialat.setBackground(DEFAULT_KEY_COLOR);
        keycommercialat.setForeground(java.awt.Color.white);
        keycommercialat.setHorizontalAlignment(SwingConstants.CENTER);
        keycommercialat.setText("@");
        keycommercialat.setFocusable(false);
        keycommercialat.setName("0x200"); // NOI18N
        keycommercialat.setOpaque(true);
        keycommercialat.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyalt.setBackground(DEFAULT_KEY_COLOR);
        keyalt.setForeground(java.awt.Color.white);
        keyalt.setHorizontalAlignment(SwingConstants.CENTER);
        keyalt.setText("Alt");
        keyalt.setFocusable(false);
        keyalt.setMaximumSize(new java.awt.Dimension(40, 40));
        keyalt.setMinimumSize(new java.awt.Dimension(40, 40));
        keyalt.setName("0x12"); // NOI18N
        keyalt.setOpaque(true);
        keyalt.setPreferredSize(new java.awt.Dimension(40, 40));
        keyalt.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyspace.setBackground(DEFAULT_KEY_COLOR);
        keyspace.setForeground(java.awt.Color.white);
        keyspace.setHorizontalAlignment(SwingConstants.CENTER);
        keyspace.setText(" ");
        keyspace.setFocusable(false);
        keyspace.setMaximumSize(new java.awt.Dimension(244, 40));
        keyspace.setMinimumSize(new java.awt.Dimension(244, 40));
        keyspace.setName("0x20"); // NOI18N
        keyspace.setOpaque(true);
        keyspace.setPreferredSize(new java.awt.Dimension(244, 40));
        keyspace.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyctrl2.setBackground(DEFAULT_KEY_COLOR);
        keyctrl2.setForeground(java.awt.Color.white);
        keyctrl2.setHorizontalAlignment(SwingConstants.CENTER);
        keyctrl2.setText("Ctrl");
        keyctrl2.setFocusable(false);
        keyctrl2.setMaximumSize(new java.awt.Dimension(40, 40));
        keyctrl2.setMinimumSize(new java.awt.Dimension(40, 40));
        keyctrl2.setName("0x11"); // NOI18N
        keyctrl2.setOpaque(true);
        keyctrl2.setPreferredSize(new java.awt.Dimension(40, 40));
        keyctrl2.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyleftarrow.setBackground(DEFAULT_KEY_COLOR);
        keyleftarrow.setForeground(java.awt.Color.white);
        keyleftarrow.setHorizontalAlignment(SwingConstants.CENTER);
        keyleftarrow.setText("<");
        keyleftarrow.setFocusable(false);
        keyleftarrow.setMaximumSize(new java.awt.Dimension(40, 40));
        keyleftarrow.setMinimumSize(new java.awt.Dimension(40, 40));
        keyleftarrow.setName("0x25"); // NOI18N
        keyleftarrow.setOpaque(true);
        keyleftarrow.setPreferredSize(new java.awt.Dimension(40, 40));
        keyleftarrow.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyuparrow.setBackground(DEFAULT_KEY_COLOR);
        keyuparrow.setForeground(java.awt.Color.white);
        keyuparrow.setHorizontalAlignment(SwingConstants.CENTER);
        keyuparrow.setText("↑");
        keyuparrow.setFocusable(false);
        keyuparrow.setMaximumSize(new java.awt.Dimension(40, 19));
        keyuparrow.setMinimumSize(new java.awt.Dimension(40, 19));
        keyuparrow.setName("0x26"); // NOI18N
        keyuparrow.setOpaque(true);
        keyuparrow.setPreferredSize(new java.awt.Dimension(40, 19));
        keyuparrow.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keydownarrow.setBackground(DEFAULT_KEY_COLOR);
        keydownarrow.setForeground(java.awt.Color.white);
        keydownarrow.setHorizontalAlignment(SwingConstants.CENTER);
        keydownarrow.setText("↓");
        keydownarrow.setFocusable(false);
        keydownarrow.setMaximumSize(new java.awt.Dimension(40, 19));
        keydownarrow.setMinimumSize(new java.awt.Dimension(40, 19));
        keydownarrow.setName("0x28"); // NOI18N
        keydownarrow.setOpaque(true);
        keydownarrow.setPreferredSize(new java.awt.Dimension(40, 19));
        keydownarrow.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyaltgr.setBackground(DEFAULT_KEY_COLOR);
        keyaltgr.setForeground(java.awt.Color.white);
        keyaltgr.setHorizontalAlignment(SwingConstants.CENTER);
        keyaltgr.setText("AltGr");
        keyaltgr.setFocusable(false);
        keyaltgr.setMaximumSize(new java.awt.Dimension(40, 40));
        keyaltgr.setMinimumSize(new java.awt.Dimension(40, 40));
        keyaltgr.setName("0x12"); // NOI18N
        keyaltgr.setOpaque(true);
        keyaltgr.setPreferredSize(new java.awt.Dimension(40, 40));
        keyaltgr.addMouseListener(new java.awt.event.MouseAdapter() {
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
                        .addComponent(keyfn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keycommercialat, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyalt, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyspace, GroupLayout.PREFERRED_SIZE, 238, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyaltgr, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(keyctrl2, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
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
                        .addComponent(keyfn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyalt, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyspace, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyctrl2, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyleftarrow, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                        .addComponent(keyaltgr, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
                    .addComponent(keyuparrow, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
                    .addComponent(keyrightarrow, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                    .addComponent(keydownarrow, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)))
        );

//        optionPanel.setBackground(java.awt.Color.black);

        keyscrolllock.setBackground(DEFAULT_KEY_COLOR);
        keyscrolllock.setForeground(java.awt.Color.white);
        keyscrolllock.setHorizontalAlignment(SwingConstants.CENTER);
        keyscrolllock.setText("<html>Scroll<br />Lock</html>");
        keyscrolllock.setFocusable(false);
        keyscrolllock.setMaximumSize(new java.awt.Dimension(40, 40));
        keyscrolllock.setMinimumSize(new java.awt.Dimension(40, 40));
        keyscrolllock.setName("0x91"); // NOI18N
        keyscrolllock.setOpaque(true);
        keyscrolllock.setPreferredSize(new java.awt.Dimension(40, 40));
        keyscrolllock.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keynumlock.setBackground(DEFAULT_KEY_COLOR);
        keynumlock.setForeground(java.awt.Color.white);
        keynumlock.setHorizontalAlignment(SwingConstants.CENTER);
        keynumlock.setText("<html>Num<br />Lock</html>");
        keynumlock.setFocusable(false);
        keynumlock.setMaximumSize(new java.awt.Dimension(40, 40));
        keynumlock.setMinimumSize(new java.awt.Dimension(40, 40));
        keynumlock.setName("0x90"); // NOI18N
        keynumlock.setOpaque(true);
        keynumlock.setPreferredSize(new java.awt.Dimension(40, 40));
        keynumlock.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyinsert.setBackground(DEFAULT_KEY_COLOR);
        keyinsert.setForeground(java.awt.Color.white);
        keyinsert.setHorizontalAlignment(SwingConstants.CENTER);
        keyinsert.setText("Insert");
        keyinsert.setFocusable(false);
        keyinsert.setMaximumSize(new java.awt.Dimension(40, 40));
        keyinsert.setMinimumSize(new java.awt.Dimension(40, 40));
        keyinsert.setName("0x9B"); // NOI18N
        keyinsert.setOpaque(true);
        keyinsert.setPreferredSize(new java.awt.Dimension(40, 40));
        keyinsert.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keydelete.setBackground(DEFAULT_KEY_COLOR);
        keydelete.setForeground(java.awt.Color.white);
        keydelete.setHorizontalAlignment(SwingConstants.CENTER);
        keydelete.setText("Delete");
        keydelete.setFocusable(false);
        keydelete.setMaximumSize(new java.awt.Dimension(40, 40));
        keydelete.setMinimumSize(new java.awt.Dimension(40, 40));
        keydelete.setName("0x7F"); // NOI18N
        keydelete.setOpaque(true);
        keydelete.setPreferredSize(new java.awt.Dimension(40, 40));
        keydelete.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyend.setBackground(DEFAULT_KEY_COLOR);
        keyend.setForeground(java.awt.Color.white);
        keyend.setHorizontalAlignment(SwingConstants.CENTER);
        keyend.setText("End");
        keyend.setFocusable(false);
        keyend.setMaximumSize(new java.awt.Dimension(40, 40));
        keyend.setMinimumSize(new java.awt.Dimension(40, 40));
        keyend.setName("0x23"); // NOI18N
        keyend.setOpaque(true);
        keyend.setPreferredSize(new java.awt.Dimension(40, 40));
        keyend.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keyhome.setBackground(DEFAULT_KEY_COLOR);
        keyhome.setForeground(java.awt.Color.white);
        keyhome.setHorizontalAlignment(SwingConstants.CENTER);
        keyhome.setText("Home");
        keyhome.setFocusable(false);
        keyhome.setMaximumSize(new java.awt.Dimension(40, 40));
        keyhome.setMinimumSize(new java.awt.Dimension(40, 40));
        keyhome.setName("0x24"); // NOI18N
        keyhome.setOpaque(true);
        keyhome.setPreferredSize(new java.awt.Dimension(40, 40));
        keyhome.addMouseListener(new java.awt.event.MouseAdapter() {
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

        keypgdn.setBackground(DEFAULT_KEY_COLOR);
        keypgdn.setForeground(java.awt.Color.white);
        keypgdn.setHorizontalAlignment(SwingConstants.CENTER);
        keypgdn.setText("PgDn");
        keypgdn.setFocusable(false);
        keypgdn.setMaximumSize(new java.awt.Dimension(40, 40));
        keypgdn.setMinimumSize(new java.awt.Dimension(40, 40));
        keypgdn.setName("0x22"); // NOI18N
        keypgdn.setOpaque(true);
        keypgdn.setPreferredSize(new java.awt.Dimension(40, 40));
        keypgdn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                keyClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                keyMouseEntered(evt);
            }
            public void mouseExited(MouseEvent evt) {
                keyMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                keyPressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                keyReleased(evt);
            }
        });

        keypgup.setBackground(DEFAULT_KEY_COLOR);
        keypgup.setForeground(java.awt.Color.white);
        keypgup.setHorizontalAlignment(SwingConstants.CENTER);
        keypgup.setText("PgUP");
        keypgup.setFocusable(false);
        keypgup.setMaximumSize(new java.awt.Dimension(40, 40));
        keypgup.setMinimumSize(new java.awt.Dimension(40, 40));
        keypgup.setName("0x21"); // NOI18N
        keypgup.setOpaque(true);
        keypgup.setPreferredSize(new java.awt.Dimension(40, 40));
        keypgup.addMouseListener(new java.awt.event.MouseAdapter() {
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

//        copyright.setBackground(new java.awt.Color(0, 0, 0));
//        copyright.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
//        copyright.setForeground(java.awt.Color.gray);
//        copyright.setHorizontalAlignment(SwingConstants.CENTER);
//        copyright.setText("Â© Codeforwin");
//        copyright.setToolTipText("Developed by Pankaj Prakash");
//        copyright.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
//        copyright.setFocusable(false);
//        copyright.setMaximumSize(new java.awt.Dimension(40, 40));
//        copyright.setMinimumSize(new java.awt.Dimension(40, 40));
//        copyright.setOpaque(true);
//        copyright.setPreferredSize(new java.awt.Dimension(40, 40));
//        copyright.addMouseListener(new java.awt.event.MouseAdapter() {
//            public void mouseClicked(java.awt.event.MouseEvent evt) {
//                copyrightMouseClicked(evt);
//            }
//            public void mouseEntered(java.awt.event.MouseEvent evt) {
//                copyrightkeyMouseEntered(evt);
//            }
//            public void mouseExited(java.awt.event.MouseEvent evt) {
//                copyrightkeyMouseExited(evt);
//            }
//        });

//        numericpadCombobox.setBackground(java.awt.Color.black);
//        numericpadCombobox.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
//        numericpadCombobox.setForeground(java.awt.Color.white);
//        numericpadCombobox.setSelected(true);
//        numericpadCombobox.setText("Numeric pad");
//        numericpadCombobox.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
//        numericpadCombobox.addItemListener(new java.awt.event.ItemListener() {
//            public void itemStateChanged(java.awt.event.ItemEvent evt) {
//                numericpadComboboxItemStateChanged(evt);
//            }
//        });

//        GroupLayout optionPanelLayout = new GroupLayout(optionPanel);
//        optionPanel.setLayout(optionPanelLayout);
//        optionPanelLayout.setHorizontalGroup(
//            optionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//            .addGroup(optionPanelLayout.createSequentialGroup()
//                .addGroup(optionPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
////                    .addComponent(copyright, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//                    .addGroup(GroupLayout.Alignment.LEADING, optionPanelLayout.createSequentialGroup()
//                        .addComponent(keyscrolllock, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                        .addGap(4, 4, 4)
//                        .addComponent(keynumlock, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
//                    .addGroup(GroupLayout.Alignment.LEADING, optionPanelLayout.createSequentialGroup()
//                        .addComponent(keydelete, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                        .addGap(4, 4, 4)
//                        .addComponent(keyinsert, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
//                    .addGroup(GroupLayout.Alignment.LEADING, optionPanelLayout.createSequentialGroup()
//                        .addComponent(keyhome, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                        .addGap(4, 4, 4)
//                        .addComponent(keyend, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
//                    .addGroup(GroupLayout.Alignment.LEADING, optionPanelLayout.createSequentialGroup()
//                        .addComponent(keypgup, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                        .addGap(4, 4, 4)
//                        .addComponent(keypgdn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
////                    .addComponent(numericpadCombobox, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//                .addGap(0, 0, 0)
//        )));

//        optionPanelLayout.setVerticalGroup(
//            optionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//            .addGroup(optionPanelLayout.createSequentialGroup()
//                .addGroup(optionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                    .addComponent(keyscrolllock, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                    .addComponent(keynumlock, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
//                .addGap(4, 4, 4)
//                .addGroup(optionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                    .addComponent(keydelete, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                    .addComponent(keyinsert, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
//                .addGap(4, 4, 4)
//                .addGroup(optionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                    .addComponent(keyhome, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                    .addComponent(keyend, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
//                .addGap(4, 4, 4)
//                .addGroup(optionPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
//                    .addComponent(keypgup, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
//                    .addComponent(keypgdn, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
//                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
////                .addComponent(numericpadCombobox)
//                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
////                .addComponent(copyright, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE)
//                .addGap(0, 0, 0))
//        );

        GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(functionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(alphabetPanel, GroupLayout.PREFERRED_SIZE, 632, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
//                .addComponent(optionPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
//                .addComponent(numpadPanel, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(functionPanel, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(alphabetPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
//            .addComponent(numpadPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
//            .addComponent(optionPanel, GroupLayout.PREFERRED_SIZE, 211, GroupLayout.PREFERRED_SIZE)
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
    }// </editor-fold>//GEN-END:initComponents

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
    private JLabel keydelete;
    private JLabel keydot;
    private JLabel keydownarrow;
    private JLabel keye;
    private JLabel keyend;
    private JLabel keyenter;
    private JLabel keyequal;
    private JLabel keyf;
    private JLabel keyfn;
    private JLabel keyfslash;
    private JLabel keyg;
    private JLabel keyh;
    private JLabel keyhome;
    private JLabel keyi;
    private JLabel keyinsert;
    private JLabel keyj;
    private JLabel keyk;
    private JLabel keyl;
    private JLabel keyleftarrow;
    private JLabel keym;
    private JLabel keyminus;
    private JLabel keyn;
    private JLabel keynum0;
    private JLabel keynum1;
    private JLabel keynum2;
    private JLabel keynum3;
    private JLabel keynum4;
    private JLabel keynum5;
    private JLabel keynum6;
    private JLabel keynum7;
    private JLabel keynum8;
    private JLabel keynum9;
    private JLabel keynumdivide;
    private JLabel keynumdot;
    private JLabel keynumenter;
    private JLabel keynumlock;
    private JLabel keynumminus;
    private JLabel keynummult;
    private JLabel keynumplus;
    private JLabel keyo;
    private JLabel keyopenbigbracket;
    private JLabel keyp;
    private JLabel keypgdn;
    private JLabel keypgup;
    private JLabel keyprtscr;
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
