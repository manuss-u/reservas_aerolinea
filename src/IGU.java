
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import reservasaerolinea.Reserva;

public class IGU extends javax.swing.JFrame {
    
    private JButton ultimoBotonSeleccionado = null;
    private String codigoAsientoActual = null;
    
    private Reserva[] reservas = new Reserva[24];

    private JButton[] btnAsientos;
    
    public IGU() {
        initComponents(); 
        
        btnAsientos = new JButton[] {
            btnA1, btnB1, btnC1, btnD1,
            btnA2, btnB2, btnC2, btnD2,
            btnA3, btnB3, btnC3, btnD3,
            btnA4, btnB4, btnC4, btnD4,
            btnA5, btnB5, btnC5, btnD5,
            btnA6, btnB6, btnC6, btnD6
        };

        initReservas();
        
        txtAsiento.setBorder(new CompoundBorder (
            txtAsiento.getBorder(),
            new EmptyBorder(0, 5, 0, 5)
        ));
        txtNombre.setBorder(new CompoundBorder (
            txtNombre.getBorder(),
            new EmptyBorder(0, 5, 0, 5)
        ));
        
        ocultarPanelPasajero();
        actualizarTodosLosBotones();
    }
    
    
        private void initReservas() {
        char[] cols = {'A','B','C','D'};
        int k = 0;
        for (int fila = 1; fila <= 6; fila++) {
            for (int c = 0; c < 4; c++) {
                String codigo = cols[c] + String.valueOf(fila);
                reservas[k] = new Reserva(codigo);
                k++;
            }
        }
    }

    // ================== TU MÉTODO EXISTENTE ==================
    // Llamado por cada botón: seleccionarAsiento(btnA1, "A1"), etc.
    private void seleccionarAsiento(JButton boton, String codigoAsiento) {
        // devolver el color del último seleccionado (si era otro)
        if (ultimoBotonSeleccionado != null && ultimoBotonSeleccionado != boton) {
            ultimoBotonSeleccionado.setBackground(new java.awt.Color(240, 240, 240));
        }

        // guardar selección actual
        ultimoBotonSeleccionado = boton;
        codigoAsientoActual     = codigoAsiento;

        // marcar visualmente el botón actual
        boton.setBackground(new java.awt.Color(200, 200, 200));

        // mostrar panel y asiento
        pnlPasajero.setVisible(true);
        txtAsiento.setText(codigoAsiento);

        // cargar nombre si ya estaba reservado; limpiar si está libre
        int idx = codigoAIndice(codigoAsiento);
        if (idx >= 0) {
            Reserva r = reservas[idx];
            txtNombre.setText(r.isOcupado() ? r.getNombre() : "");
        }

        pnlPasajero.revalidate();
        pnlPasajero.repaint();
    }

    // ================== PANEL PASAJERO ==================

    // Regla súper simple:
    // - Si txtNombre está vacío => cancelar reserva
    // - Si txtNombre tiene texto => reservar/actualizar no

    
    
    
    
    private void ocultarPanelPasajero() {
        pnlPasajero.setVisible(false);
        txtAsiento.setText("");
        txtNombre.setText("");

        // Si quieres liberar la “selección visual”, puedes devolver color:
        if (ultimoBotonSeleccionado != null) {
            // Si quieres que VUELVA al color según su estado (reservado o libre):
            int idx = codigoAIndice(codigoAsientoActual);
            if (idx >= 0) actualizarBotonPorIndice(idx);
        }

        // Ahora sí, limpia referencias
        ultimoBotonSeleccionado = null;
        codigoAsientoActual     = null;
    }
    
    
    // ================== UTILIDADES BÁSICAS ==================
    // Convierte "A1".."D6" a índice 0..23 SIN listas:
    // índice = (fila-1)*4 + columna(A=0,B=1,C=2,D=3)
    
    
    
    private int codigoAIndice(String codigo) {
        if (codigo == null || codigo.length() < 2) return -1;

        char col = Character.toUpperCase(codigo.charAt(0));
        int colIdx = (col == 'A') ? 0 : (col == 'B') ? 1 : (col == 'C') ? 2 : (col == 'D') ? 3 : -1;
        if (colIdx == -1) return -1;

        int fila;
        try { fila = Integer.parseInt(codigo.substring(1)); }
        catch (NumberFormatException ex) { return -1; }

        if (fila < 1 || fila > 6) return -1;

        return (fila - 1) * 4 + colIdx;  // 0..23
    }
    
  private void actualizarBotonPorIndice(int idx) {
        JButton b = btnAsientos[idx];
        Reserva r = reservas[idx];

        if (r.isOcupado()) {
            b.setBackground(java.awt.Color.PINK);
            b.setToolTipText("Reservado por: " + r.getNombre());
        } else {
            b.setBackground(new java.awt.Color(240, 240, 240));
            b.setToolTipText("Libre");
        }
    }


    private void actualizarTodosLosBotones() {
        for (int i = 0; i < reservas.length; i++) {
            actualizarBotonPorIndice(i);
        }
    }

    // Opcional: mostrar listas y reiniciar si tienes esos botones
    private void reiniciarSistema() {
        for (int i = 0; i < reservas.length; i++) {
            reservas[i].cancelar();
        }
        actualizarTodosLosBotones();
        ocultarPanelPasajero();
        JOptionPane.showMessageDialog(this, "Sistema reiniciado. Todos los asientos están libres.");
    }

    private void mostrarEstado() {
        String libres = "";
        String reservados = "";
        for (int i = 0; i < reservas.length; i++) {
            Reserva r = reservas[i];
            if (r.isOcupado()) {
                reservados += r.getAsiento() + " - " + r.getNombre() + "\n";
            } else {
                libres += r.getAsiento() + "\n";
            }
        }
        JTextArea ta = new JTextArea(20, 30);
        ta.setEditable(false);
        ta.setText("=== LIBRES ===\n" + libres + "\n=== RESERVADOS ===\n" + reservados);
        JScrollPane sp = new JScrollPane(ta);
        JOptionPane.showMessageDialog(this, sp, "Estado de asientos", JOptionPane.INFORMATION_MESSAGE);
    }
    
    
    private void confirmarReserva() {
        if (codigoAsientoActual == null) {
            JOptionPane.showMessageDialog(this, "No hay asiento seleccionado.");
            return;
        }

        int idx = codigoAIndice(codigoAsientoActual);
        if (idx < 0) return;

        String nombre = txtNombre.getText().trim();
        Reserva r = reservas[idx];

        if (nombre.isEmpty()) {
            // Cancelar reserva si el campo está vacío
            if (r.isOcupado()) {
                r.cancelar();
                actualizarBotonPorIndice(idx);
                JOptionPane.showMessageDialog(this, "Reserva cancelada para " + codigoAsientoActual + ".");
            } else {
                JOptionPane.showMessageDialog(this, "Ese asiento ya está libre.");
            }
        } else {
            // Reservar o actualizar
            if (r.reservar(nombre)) {
                actualizarBotonPorIndice(idx);
                JOptionPane.showMessageDialog(this, "Reserva confirmada: " + codigoAsientoActual + " para " + nombre + ".");
            } else {
                JOptionPane.showMessageDialog(this, "Nombre inválido.");
            }
        }

        ocultarPanelPasajero(); // cierra y limpia
    }
    
    private void cancelarReserva() {
        ocultarPanelPasajero();
    }
    
  

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlFondoBlanco = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        pnlAsientos = new javax.swing.JPanel();
        btnA1 = new javax.swing.JButton();
        btnB1 = new javax.swing.JButton();
        btnA2 = new javax.swing.JButton();
        btnB2 = new javax.swing.JButton();
        btnA3 = new javax.swing.JButton();
        btnB3 = new javax.swing.JButton();
        btnA4 = new javax.swing.JButton();
        btnB4 = new javax.swing.JButton();
        btnA5 = new javax.swing.JButton();
        btnB5 = new javax.swing.JButton();
        btnA6 = new javax.swing.JButton();
        btnB6 = new javax.swing.JButton();
        btnC1 = new javax.swing.JButton();
        btnD1 = new javax.swing.JButton();
        btnC2 = new javax.swing.JButton();
        btnD2 = new javax.swing.JButton();
        btnC3 = new javax.swing.JButton();
        btnD3 = new javax.swing.JButton();
        btnC4 = new javax.swing.JButton();
        btnD4 = new javax.swing.JButton();
        btnC5 = new javax.swing.JButton();
        btnD5 = new javax.swing.JButton();
        btnC6 = new javax.swing.JButton();
        btnD6 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        pnlPasajero = new javax.swing.JPanel();
        txtTextoAsiento = new javax.swing.JLabel();
        txtTextoNombre = new javax.swing.JLabel();
        txtAsiento = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        btnAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnMostrar = new javax.swing.JButton();
        btnReiniciar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlFondoBlanco.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(200, 200, 200));
        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        pnlAsientos.setBackground(new java.awt.Color(200, 200, 200));
        pnlAsientos.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnA1.setActionCommand("A1");
        btnA1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnA1ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnA1, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 163, 45, 45));

        btnB1.setActionCommand("B1");
        btnB1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnB1ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnB1, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 163, 45, 45));

        btnA2.setActionCommand("A2");
        btnA2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnA2ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnA2, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 226, 45, 45));

        btnB2.setActionCommand("B2");
        btnB2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnB2ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnB2, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 226, 45, 45));

        btnA3.setActionCommand("A3");
        btnA3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnA3ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnA3, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 289, 45, 45));

        btnB3.setActionCommand("B3");
        btnB3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnB3ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnB3, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 289, 45, 45));

        btnA4.setActionCommand("A4");
        btnA4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnA4ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnA4, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 352, 45, 45));

        btnB4.setActionCommand("B4");
        btnB4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnB4ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnB4, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 352, 45, 45));

        btnA5.setActionCommand("A5");
        btnA5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnA5ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnA5, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 415, 45, 45));

        btnB5.setActionCommand("B5");
        btnB5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnB5ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnB5, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 415, 45, 45));

        btnA6.setActionCommand("A6");
        btnA6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnA6ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnA6, new org.netbeans.lib.awtextra.AbsoluteConstraints(65, 478, 45, 45));

        btnB6.setActionCommand("B6");
        btnB6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnB6ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnB6, new org.netbeans.lib.awtextra.AbsoluteConstraints(128, 478, 45, 45));

        btnC1.setActionCommand("C1");
        btnC1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnC1ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnC1, new org.netbeans.lib.awtextra.AbsoluteConstraints(262, 163, 45, 45));

        btnD1.setActionCommand("D1");
        btnD1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnD1ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnD1, new org.netbeans.lib.awtextra.AbsoluteConstraints(325, 163, 45, 45));

        btnC2.setActionCommand("C2");
        btnC2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnC2ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnC2, new org.netbeans.lib.awtextra.AbsoluteConstraints(262, 226, 45, 45));

        btnD2.setActionCommand("D2");
        btnD2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnD2ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnD2, new org.netbeans.lib.awtextra.AbsoluteConstraints(325, 226, 45, 45));

        btnC3.setActionCommand("C3");
        btnC3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnC3ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnC3, new org.netbeans.lib.awtextra.AbsoluteConstraints(262, 289, 45, 45));

        btnD3.setActionCommand("D3");
        btnD3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnD3ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnD3, new org.netbeans.lib.awtextra.AbsoluteConstraints(325, 289, 45, 45));

        btnC4.setActionCommand("C4");
        btnC4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnC4ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnC4, new org.netbeans.lib.awtextra.AbsoluteConstraints(262, 352, 45, 45));

        btnD4.setActionCommand("D4");
        btnD4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnD4ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnD4, new org.netbeans.lib.awtextra.AbsoluteConstraints(325, 352, 45, 45));

        btnC5.setActionCommand("C5");
        btnC5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnC5ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnC5, new org.netbeans.lib.awtextra.AbsoluteConstraints(262, 415, 45, 45));

        btnD5.setActionCommand("D5");
        btnD5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnD5ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnD5, new org.netbeans.lib.awtextra.AbsoluteConstraints(325, 415, 45, 45));

        btnC6.setActionCommand("C6");
        btnC6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnC6ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnC6, new org.netbeans.lib.awtextra.AbsoluteConstraints(262, 478, 45, 45));

        btnD6.setActionCommand("D6");
        btnD6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnD6ActionPerformed(evt);
            }
        });
        pnlAsientos.add(btnD6, new org.netbeans.lib.awtextra.AbsoluteConstraints(325, 478, 45, 45));

        jLabel1.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel1.setText("B");
        pnlAsientos.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(147, 139, 26, -1));

        jLabel2.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel2.setText("A");
        pnlAsientos.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(84, 139, 26, -1));

        jLabel3.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel3.setText("D");
        pnlAsientos.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(344, 139, 26, -1));

        jLabel4.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel4.setText("1");
        pnlAsientos.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 177, 26, -1));

        jLabel5.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel5.setText("C");
        pnlAsientos.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(281, 139, 26, -1));

        jLabel6.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel6.setText("2");
        pnlAsientos.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 240, 26, -1));

        jLabel7.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel7.setText("4");
        pnlAsientos.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 366, 26, -1));

        jLabel8.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel8.setText("6");
        pnlAsientos.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 490, 26, -1));

        jLabel9.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel9.setText("5");
        pnlAsientos.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 432, 26, -1));

        jLabel10.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel10.setText("3");
        pnlAsientos.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(35, 303, 18, -1));

        jLabel11.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel11.setText("1");
        pnlAsientos.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(388, 177, 26, -1));

        jLabel12.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel12.setText("2");
        pnlAsientos.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(388, 237, 26, -1));

        jLabel13.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel13.setText("3");
        pnlAsientos.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(388, 302, 18, -1));

        jLabel14.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel14.setText("4");
        pnlAsientos.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(388, 363, 26, -1));

        jLabel15.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel15.setText("5");
        pnlAsientos.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(388, 428, 26, -1));

        jLabel16.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        jLabel16.setText("6");
        pnlAsientos.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 490, 26, -1));

        pnlPasajero.setBackground(new java.awt.Color(255, 255, 255));
        pnlPasajero.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pasajero", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial Black", 1, 14))); // NOI18N
        pnlPasajero.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTextoAsiento.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        txtTextoAsiento.setText("Asiento:");
        pnlPasajero.add(txtTextoAsiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 83, -1, -1));

        txtTextoNombre.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        txtTextoNombre.setText("Nombre:");
        pnlPasajero.add(txtTextoNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 155, -1, -1));

        txtAsiento.setEditable(false);
        txtAsiento.setBackground(new java.awt.Color(204, 204, 204));
        txtAsiento.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtAsiento.setBorder(null);
        pnlPasajero.add(txtAsiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 119, 71, 24));

        txtNombre.setBackground(new java.awt.Color(204, 204, 204));
        txtNombre.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtNombre.setBorder(null);
        pnlPasajero.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(32, 191, 306, 24));

        btnAceptar.setBackground(new java.awt.Color(36, 74, 185));
        btnAceptar.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        btnAceptar.setForeground(new java.awt.Color(255, 255, 255));
        btnAceptar.setText("Aceptar");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });
        pnlPasajero.add(btnAceptar, new org.netbeans.lib.awtextra.AbsoluteConstraints(71, 260, -1, -1));

        btnCancelar.setBackground(new java.awt.Color(36, 74, 185));
        btnCancelar.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        btnCancelar.setForeground(new java.awt.Color(255, 255, 255));
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        pnlPasajero.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(219, 260, -1, -1));

        btnMostrar.setBackground(new java.awt.Color(36, 74, 185));
        btnMostrar.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        btnMostrar.setForeground(new java.awt.Color(255, 255, 255));
        btnMostrar.setText("Mostrar");
        btnMostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarActionPerformed(evt);
            }
        });

        btnReiniciar.setBackground(new java.awt.Color(36, 74, 185));
        btnReiniciar.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        btnReiniciar.setForeground(new java.awt.Color(255, 255, 255));
        btnReiniciar.setText("Reiniciar");
        btnReiniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReiniciarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFondoBlancoLayout = new javax.swing.GroupLayout(pnlFondoBlanco);
        pnlFondoBlanco.setLayout(pnlFondoBlancoLayout);
        pnlFondoBlancoLayout.setHorizontalGroup(
            pnlFondoBlancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFondoBlancoLayout.createSequentialGroup()
                .addComponent(pnlAsientos, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnlFondoBlancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFondoBlancoLayout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addComponent(pnlPasajero, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlFondoBlancoLayout.createSequentialGroup()
                        .addGap(169, 169, 169)
                        .addComponent(btnMostrar)
                        .addGap(75, 75, 75)
                        .addComponent(btnReiniciar)))
                .addGap(0, 79, Short.MAX_VALUE))
        );
        pnlFondoBlancoLayout.setVerticalGroup(
            pnlFondoBlancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlFondoBlancoLayout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addComponent(pnlPasajero, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlFondoBlancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnMostrar)
                    .addComponent(btnReiniciar))
                .addGap(49, 49, 49))
            .addComponent(pnlAsientos, javax.swing.GroupLayout.DEFAULT_SIZE, 612, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlFondoBlanco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlFondoBlanco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnA1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnA1ActionPerformed
        seleccionarAsiento(btnA1, "A1");
    }//GEN-LAST:event_btnA1ActionPerformed

    private void btnB1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnB1ActionPerformed
        seleccionarAsiento(btnB1, "B1");
    }//GEN-LAST:event_btnB1ActionPerformed

    private void btnA2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnA2ActionPerformed
        seleccionarAsiento(btnA2, "A2");
    }//GEN-LAST:event_btnA2ActionPerformed

    private void btnB2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnB2ActionPerformed
        seleccionarAsiento(btnB2, "B2");
    }//GEN-LAST:event_btnB2ActionPerformed

    private void btnA3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnA3ActionPerformed
        seleccionarAsiento(btnA3, "A3");
    }//GEN-LAST:event_btnA3ActionPerformed

    private void btnB3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnB3ActionPerformed
        seleccionarAsiento(btnB3, "B3");
    }//GEN-LAST:event_btnB3ActionPerformed

    private void btnA4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnA4ActionPerformed
        seleccionarAsiento(btnA4, "A4");
    }//GEN-LAST:event_btnA4ActionPerformed

    private void btnB4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnB4ActionPerformed
        seleccionarAsiento(btnB4, "B4");
    }//GEN-LAST:event_btnB4ActionPerformed

    private void btnA5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnA5ActionPerformed
        seleccionarAsiento(btnA5, "A5");
    }//GEN-LAST:event_btnA5ActionPerformed

    private void btnB5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnB5ActionPerformed
        seleccionarAsiento(btnB5, "B5");
    }//GEN-LAST:event_btnB5ActionPerformed

    private void btnA6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnA6ActionPerformed
        seleccionarAsiento(btnA6, "A6");
    }//GEN-LAST:event_btnA6ActionPerformed

    private void btnB6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnB6ActionPerformed
        seleccionarAsiento(btnB6, "B6");
    }//GEN-LAST:event_btnB6ActionPerformed

    private void btnC1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnC1ActionPerformed
        seleccionarAsiento(btnC1, "C1");
    }//GEN-LAST:event_btnC1ActionPerformed

    private void btnD1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnD1ActionPerformed
        seleccionarAsiento(btnD1, "D1");
    }//GEN-LAST:event_btnD1ActionPerformed

    private void btnC2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnC2ActionPerformed
        seleccionarAsiento(btnC2, "C2");
    }//GEN-LAST:event_btnC2ActionPerformed

    private void btnD2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnD2ActionPerformed
        seleccionarAsiento(btnD2, "D2");
    }//GEN-LAST:event_btnD2ActionPerformed

    private void btnC3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnC3ActionPerformed
        seleccionarAsiento(btnC3, "C3");
    }//GEN-LAST:event_btnC3ActionPerformed

    private void btnD3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnD3ActionPerformed
        seleccionarAsiento(btnD3, "D3");
    }//GEN-LAST:event_btnD3ActionPerformed

    private void btnC4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnC4ActionPerformed
        seleccionarAsiento(btnC4, "C4");
    }//GEN-LAST:event_btnC4ActionPerformed

    private void btnD4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnD4ActionPerformed
        seleccionarAsiento(btnD4, "D4");
    }//GEN-LAST:event_btnD4ActionPerformed

    private void btnC5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnC5ActionPerformed
        seleccionarAsiento(btnC5, "C5");
    }//GEN-LAST:event_btnC5ActionPerformed

    private void btnD5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnD5ActionPerformed
        seleccionarAsiento(btnD5, "D5");
    }//GEN-LAST:event_btnD5ActionPerformed

    private void btnC6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnC6ActionPerformed
        seleccionarAsiento(btnC6, "C6");
    }//GEN-LAST:event_btnC6ActionPerformed

    private void btnD6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnD6ActionPerformed
        seleccionarAsiento(btnD6, "D6");
    }//GEN-LAST:event_btnD6ActionPerformed

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        confirmarReserva();
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelarReserva();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnMostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarActionPerformed
        mostrarEstado();
    }//GEN-LAST:event_btnMostrarActionPerformed

    private void btnReiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReiniciarActionPerformed
        reiniciarSistema();
    }//GEN-LAST:event_btnReiniciarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(IGU.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IGU.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IGU.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IGU.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new IGU().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnA1;
    private javax.swing.JButton btnA2;
    private javax.swing.JButton btnA3;
    private javax.swing.JButton btnA4;
    private javax.swing.JButton btnA5;
    private javax.swing.JButton btnA6;
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnB1;
    private javax.swing.JButton btnB2;
    private javax.swing.JButton btnB3;
    private javax.swing.JButton btnB4;
    private javax.swing.JButton btnB5;
    private javax.swing.JButton btnB6;
    private javax.swing.JButton btnC1;
    private javax.swing.JButton btnC2;
    private javax.swing.JButton btnC3;
    private javax.swing.JButton btnC4;
    private javax.swing.JButton btnC5;
    private javax.swing.JButton btnC6;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnD1;
    private javax.swing.JButton btnD2;
    private javax.swing.JButton btnD3;
    private javax.swing.JButton btnD4;
    private javax.swing.JButton btnD5;
    private javax.swing.JButton btnD6;
    private javax.swing.JButton btnMostrar;
    private javax.swing.JButton btnReiniciar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel pnlAsientos;
    private javax.swing.JPanel pnlFondoBlanco;
    private javax.swing.JPanel pnlPasajero;
    private javax.swing.JTextField txtAsiento;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JLabel txtTextoAsiento;
    private javax.swing.JLabel txtTextoNombre;
    // End of variables declaration//GEN-END:variables
}
