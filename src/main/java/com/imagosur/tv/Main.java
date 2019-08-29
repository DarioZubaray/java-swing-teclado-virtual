package com.imagosur.tv;

import java.awt.EventQueue;
import java.awt.im.InputContext;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        checkApplicationRunning(100);
        checkContextLocale();
        loadsLookAndFeel();

        int heigth = getHeigthParam(args);
        int width = getWidthParam(args);

        EventQueue.invokeLater(() -> {
            new KeyboardUI(heigth, width).setVisible(true);
        });
    }

    public static void checkApplicationRunning(int port) {
        try {
            @SuppressWarnings("resource")
            ServerSocket ss = new ServerSocket();
            ss.bind(new InetSocketAddress(port));
            System.out.println("Aplicacion iniciada, ocupando el puerto local: " + port);
        } catch (SocketException e) {
            System.err.println("La aplicacion ya se encuentra corriendo, finalizando.");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("La aplicacion encontró algun problema de arranque: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void checkContextLocale() {
        InputContext context = InputContext.getInstance();
        System.out.println("ContextLocale: " + context.getLocale().toString());
    }

    public static int getHeigthParam(String[] args) {
        if (args != null && args.length > 0) {
            String primerParametro = args[0];
            try {
                return Integer.parseInt(primerParametro);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    public static int getWidthParam(String[] args) {
        if (args != null && args.length > 1) {
            String segundoParametro = args[1];
            try {
                return Integer.parseInt(segundoParametro);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    public static void loadsLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException ex) {
            System.out.println("No se pudo cargar el look and feel");
        }

    }
}
