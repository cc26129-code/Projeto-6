package br.com.projeto6;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Não foi possível aplicar o tema do sistema: " + e.getMessage());
            }

            JOptionPane.showMessageDialog(
                    null,
                    "Estrutura inicial do Sistema de Reservas criada.",
                    "Projeto 6",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });
    }
}
