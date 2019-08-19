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
                System.err.println(ex.getMessage());
            }
        }
        
        for(Component component : this.functionPanel.getComponents()) {
            try {
                JLabel key = (JLabel) component;
                
                functionKeys.add(key);
            } catch(ClassCastException ex) {
              System.err.println(ex.getMessage());
            }
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
    }

    private void initComponents() {

        JPanel mainPanel = new JPanel();
        functionPanel = new JPanel();
        JLabel keybackquote = initLabel("<html>~<br />&nbsp;&nbsp;`</html>", "0xC0");
        JLabel key1 = initLabel("<html>! <br />&nbsp;&nbsp;1</html>", "0x31");
        JLabel key2 = initLabel("<html>\"<br />&nbsp;&nbsp;2</html>", "0x32");
        JLabel key3 = initLabel("<html># <br />&nbsp;&nbsp;3</html>", "0x33");
        JLabel key4 = initLabel("<html>$ <br />&nbsp;&nbsp;4</html>", "0x34");
        JLabel key5 = initLabel("<html>% <br />&nbsp;&nbsp;5</html>", "0x35");
        JLabel key6 = initLabel("<html>& <br />&nbsp;&nbsp;6</html>", "0x36");
        JLabel key7 = initLabel("<html>/ <br />&nbsp;&nbsp;7</html>", "0x37");
        JLabel key8 = initLabel("<html>( <br />&nbsp;&nbsp;8</html>", "0x38");
        JLabel key9 = initLabel("<html>) <br />&nbsp;&nbsp;9</html>", "0x39");
        JLabel key0 = initLabel("<html>= <br />&nbsp;&nbsp;0</html>", "0x40");
        JLabel keyminus = initLabel("<html>_<br />&nbsp;&nbsp;-</html>", "0x2D");
        JLabel keyequal = initLabel("<html>+<br />&nbsp;&nbsp;=</html>", "0x3D");
        keybackspace = initLabelWithDimension("Backspace", "0x08", 60, 40);
        alphabetPanel = new JPanel();
        JLabel keytab = initLabelWithDimension("Tab", "0x09", 60, 40);
        JLabel keyq = initLabel("q", "0x51");
        JLabel keyw = initLabel("w", "0x57");
        JLabel keye = initLabel("e", "0x45");
        JLabel keyr = initLabel("r", "0x52");
        JLabel keyt = initLabel("t", "0x54");
        JLabel keyy = initLabel("y", "0x59");
        JLabel keyu = initLabel("u", "0x55");
        JLabel keyi = initLabel("i", "0x49");
        JLabel keyo = initLabel("o", "0x4F");
        JLabel keyp = initLabel("p", "0x50");
        JLabel keyopenbigbracket = initLabel("<html>{<br />&nbsp;&nbsp;[</html>", "0x5B");
        JLabel keyclosebigbracket = initLabel("<html>}<br />&nbsp;&nbsp;]</html>", "0x5D");
        JLabel keybslash = initLabel("<html>|<br />&nbsp;&nbsp;\\</html>", "0x5C");
        JLabel keyj = initLabel("j", "0x4A");
        JLabel keyk = initLabel("k", "0x4B");
        JLabel keyl = initLabel("l", "0x4C");
        keycaps = initLabelWithDimension("Mayus", "0x14", 70, 40);
        JLabel keysemicolon = initLabel("<html>:<br />&nbsp;&nbsp;;</html>", "0x3B");
        JLabel keyquote = initLabel("<html>\"<br />&nbsp;&nbsp;'</html>", "0xDE");
        JLabel keya = initLabel("a", "0x41");
        JLabel keyenter = initLabelWithDimension("Enter", "0x0A", 76, 40);
        JLabel keys = initLabel("s", "0x53");
        JLabel keyd = initLabel("d", "0x44");
        JLabel keyf = initLabel("f", "0x46");
        JLabel keyg = initLabel("g", "0x47");
        JLabel keyh = initLabel("h", "0x48");
        JLabel keym = initLabel("m", "0x4D");
        JLabel keycomma = initLabel("<html>&lt;&nbsp;&nbsp;,</html>", "0x2C");
        JLabel keydot = initLabel("<html>&gt;&nbsp;&nbsp;.</html>", "0x2E");
        keyshiftl = initLabelWithDimension("Shift", "0x10", 87, 40);
        JLabel keyfslash = initLabel("/", "0x2F");
        JLabel keyz = initLabel("z", "0x5A");
        keyshiftr = initLabel("Shift", "0x10");
        JLabel keyx = initLabel("x", "0x58");
        JLabel keyc = initLabel("c", "0x43");
        JLabel keyv = initLabel("v", "0x56");
        JLabel keyb = initLabel("b", "0x42");
        JLabel keyn = initLabel("n", "0x4E");
        JLabel keyrightarrow = initLabel(">", "0x27");
        keyctrl1 = initLabel("Ctrl", "0x11");
        JLabel keycommercialat = initLabel("@", "0x200");
        keyalt = initLabel("Alt", "0x12");
        JLabel keyspace = initLabelWithDimension(" ", "0x20", 244, 40);
        keyctrl2 = initLabel("Ctrl", "0x11");
        JLabel keyleftarrow = initLabel("<", "0x26");
        JLabel keyuparrow = initLabelWithDimension("^", "0x26", 40, 19);
        JLabel keydownarrow = initLabelWithDimension(" ", "0x28", 40, 19);
        keyaltgr = initLabel("Alt", "0x12");

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

    private JLabel keyalt;
    private JLabel keyaltgr;
    private JLabel keybackspace;
    private JLabel keycaps;
    private JLabel keyctrl1;
    private JLabel keyctrl2;
    private JLabel keyshiftl;
    private JLabel keyshiftr;

}
