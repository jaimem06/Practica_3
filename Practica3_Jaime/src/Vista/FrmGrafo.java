package Vista;

import Controlador.Exceptions.VerticeException;
import Controlador.Estrella.Estrella_Controller;
import Controlador.Estrella.ImplementarFloyd;
import Controlador.TDA_Grafos.GrafoDE;
import Controlador.TDA_Lista.ListaEnlazada;
import Vista.Tablas.ModeloTablaGrafo;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * @author Jaime Mendoza
 */
public class FrmGrafo extends javax.swing.JFrame {

    Estrella_Controller grafo_persona = new Estrella_Controller();
    private GrafoDE grafoD = new GrafoDE(0, String.class);
    private ModeloTablaGrafo tablemodel = new ModeloTablaGrafo();

    public FrmGrafo() throws Exception {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void generarGrafo() throws Exception {
        if (!String.valueOf(this.nombreVertice.getText()).equalsIgnoreCase("")
                && -1 == grafoD.obtenerCodigo(String.valueOf(this.nombreVertice.getText())))
        {
            GrafoDE aux = grafoD;

            this.grafoD = new GrafoDE(aux.numVertices() + 1, String.class);
            for (int i = 1; i <= aux.numVertices(); i++)
            {
                grafoD.etiquetarVertice(i, aux.obtenerEtiqueta(i));
            }
            for (int i = 1; i <= aux.numVertices(); i++)
            {
                for (int j = 0; j < aux.adyacente(i).getSize(); j++)
                {
                    grafoD.insertarAristaE(aux.obtenerEtiqueta(i),
                            aux.obtenerEtiqueta(aux.adyacente(i).obtenerDato(j).getDestino()),
                            (Double) aux.existeAristaE(aux.obtenerEtiqueta(i),
                                    aux.obtenerEtiqueta(aux.adyacente(i).obtenerDato(j).getDestino()))[1]);
                }
            }
            grafoD.etiquetarVertice(grafoD.numVertices(), String.valueOf(this.nombreVertice.getText()));

        } else
        {
            JOptionPane.showMessageDialog(null, "Error con nombre", "ERROR",
                    JOptionPane.ERROR_MESSAGE);
        }
        Limpiar();
        for (int i = 1; i <= grafoD.numVertices(); i++)
        {
            cbxFin.addItem(String.valueOf(grafoD.obtenerEtiqueta(i)));
            cbxOrigen1.addItem(String.valueOf(grafoD.obtenerEtiqueta(i)));
            cbxDestino.addItem(String.valueOf(grafoD.obtenerEtiqueta(i)));
            cbxinicio.addItem(String.valueOf(grafoD.obtenerEtiqueta(i)));
            cargarTabla();
            this.nombreVertice.setText(null);
        }
        System.out.println(grafoD.numAristas() + "  " + grafoD.numVertices());
    }

    public DefaultTableModel getTableListaDis(Boolean todosDatos) throws Exception {
        ListaEnlazada[] datos_lista = grafoD.dijkStra(String.valueOf(cbxinicio.getSelectedItem().toString()), String.valueOf(cbxFin.getSelectedItem().toString()), todosDatos);
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Vertice llegada");
        model.addColumn("Peso");
        model.addColumn("Recorrido");
        String datos[] = new String[3];
        for (int i = 0; i < datos_lista.length; i++)
        {
            datos[0] = String.valueOf(datos_lista[i].obtenerDato(0));
            datos[1] = String.valueOf(datos_lista[i].obtenerDato(1));
            datos[2] = String.valueOf(datos_lista[i].obtenerDato(2));
            model.addRow(datos);
        }
        return model;
    }

    private void cargarTabla() throws Exception {
        tablemodel.setGrafoED(grafoD);
        tblTabla.setModel(tablemodel);
        tablemodel.fireTableStructureChanged();
        tblTabla.updateUI();
    }

    private void adyacencias() throws Exception {
        String i = String.valueOf(cbxOrigen1.getSelectedItem().toString());
        String j = String.valueOf(cbxDestino.getSelectedItem().toString());

        if (i.equalsIgnoreCase(j))
        {
            JOptionPane.showMessageDialog(null, "Vertices iguales", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else
        {
            try
            {
                grafoD.insertarAristaE(i, j, Double.parseDouble(this.peso.getText()));
                cargarTabla();
            } catch (VerticeException ex)
            {
                System.out.println("ERROR EN INSERTAR ADYACENCIA: " + ex);
                JOptionPane.showMessageDialog(null, "No se puede insertar", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
        this.peso.setText(null);
    }

    private void caminoFloyd() {

        Integer origen = (cbxinicio.getSelectedIndex() + 1);
        Integer destino = (cbxFin.getSelectedIndex() + 1);
        if (origen == destino)
        {
            JOptionPane.showMessageDialog(null, "Los vertices no deben ser iguales", "ERROR", JOptionPane.ERROR_MESSAGE);
        } else
        {
            try
            {
                ListaEnlazada<Integer> lista = grafo_persona.getGrafoEND().caminosFloyd(origen, destino);
                String[] aux = new String[lista.getSize()];
                for (int i = 0; i < lista.getSize(); i++)
                {
                    aux[i] = grafo_persona.getGrafoEND().obtenerEtiqueta(lista.obtenerDato(i)).toString();
                }
                ListaFloyd.setListData(aux);
            } catch (Exception ex)
            {
                JOptionPane.showMessageDialog(null, ex.toString(), "ERROR", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void caminoDijsktra() {
        try
        {
            String dato_a = "", dato_b = "";

            Integer[] a = grafoD.toArrayDFS();
            //Integer[] b = grafoD.BPA(1);
            System.out.println("Camino");
            System.out.println(Arrays.toString(a));
            //System.out.println(Arrays.toString(b));

            for (int i = 0; i < a.length - 1; i++)
            {
                dato_a = dato_a + " " + grafoD.obtenerEtiqueta(a[i]);
            }
            //for (int i = 0; i < b.length - 1; i++)
            {
                // dato_b = dato_b + " " + grafoD.obtenerEtiqueta(b[i]);
            }
            this.textBPA.setText(dato_a);
            this.textPraBBP.setText(dato_b);
        } catch (Exception ex)
        {
            //Logger.getLogger(FrmGrafo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void Limpiar() {
        cbxFin.removeAllItems();
        cbxOrigen1.removeAllItems();
        cbxDestino.removeAllItems();
        cbxinicio.removeAllItems();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TipoGrafo = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        cbxOrigen1 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        cbxDestino = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        btnOk1 = new javax.swing.JButton();
        peso = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        nombreVertice = new javax.swing.JTextField();
        btnModificar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTabla = new javax.swing.JTable();
        btnMostrar = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        btn_Buscar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaDijkstra = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cbxinicio = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        cbxFin = new javax.swing.JComboBox<>();
        btnBuscarDijkstra = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        textPraBBP = new javax.swing.JLabel();
        textBPA = new javax.swing.JLabel();
        btnBuscarFloyd = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        ListaFloyd = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(153, 204, 255));
        jPanel1.setLayout(null);

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Origen");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(600, 170, 50, 16);

        cbxOrigen1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0" }));
        jPanel1.add(cbxOrigen1);
        cbxOrigen1.setBounds(650, 170, 90, 22);

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Destino");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(750, 170, 50, 16);

        cbxDestino.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0" }));
        jPanel1.add(cbxDestino);
        cbxDestino.setBounds(800, 170, 100, 22);

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Peso:");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(600, 210, 48, 16);

        btnOk1.setBackground(new java.awt.Color(51, 153, 255));
        btnOk1.setForeground(new java.awt.Color(0, 0, 0));
        btnOk1.setText("Conectar");
        btnOk1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOk1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnOk1);
        btnOk1.setBounds(770, 210, 92, 22);
        jPanel1.add(peso);
        peso.setBounds(650, 210, 100, 22);

        jLabel7.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Nuevo vertice:");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(20, 20, 113, 20);
        jPanel1.add(nombreVertice);
        nombreVertice.setBounds(20, 40, 150, 22);

        btnModificar.setBackground(new java.awt.Color(51, 153, 255));
        btnModificar.setForeground(new java.awt.Color(0, 0, 0));
        btnModificar.setText("Agregar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });
        jPanel1.add(btnModificar);
        btnModificar.setBounds(200, 40, 75, 22);

        tblTabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblTabla);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(20, 90, 560, 170);

        btnMostrar.setBackground(new java.awt.Color(51, 153, 255));
        btnMostrar.setForeground(new java.awt.Color(0, 0, 0));
        btnMostrar.setText("Mostrar");
        btnMostrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMostrarActionPerformed(evt);
            }
        });
        jPanel1.add(btnMostrar);
        btnMostrar.setBounds(510, 280, 75, 60);

        jLabel12.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("BBP");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(30, 260, 50, 20);

        jLabel10.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("BBA");
        jPanel1.add(jLabel10);
        jLabel10.setBounds(260, 260, 50, 20);

        btn_Buscar.setBackground(new java.awt.Color(51, 153, 255));
        btn_Buscar.setForeground(new java.awt.Color(0, 0, 0));
        btn_Buscar.setText("BUSCAR");
        btn_Buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BuscarActionPerformed(evt);
            }
        });
        jPanel1.add(btn_Buscar);
        btn_Buscar.setBounds(20, 350, 90, 22);

        tablaDijkstra.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaDijkstra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaDijkstraMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablaDijkstra);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(210, 470, 380, 170);

        jLabel5.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Dijkstra");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(210, 400, 102, 20);

        jLabel8.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Origen");
        jPanel1.add(jLabel8);
        jLabel8.setBounds(10, 500, 68, 16);

        cbxinicio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0" }));
        cbxinicio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxinicioActionPerformed(evt);
            }
        });
        jPanel1.add(cbxinicio);
        cbxinicio.setBounds(70, 500, 130, 22);

        jLabel9.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Destino");
        jPanel1.add(jLabel9);
        jLabel9.setBounds(10, 560, 78, 16);

        cbxFin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0" }));
        jPanel1.add(cbxFin);
        cbxFin.setBounds(70, 560, 130, 22);

        btnBuscarDijkstra.setBackground(new java.awt.Color(51, 153, 255));
        btnBuscarDijkstra.setForeground(new java.awt.Color(0, 0, 0));
        btnBuscarDijkstra.setText("Buscar Camino");
        btnBuscarDijkstra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarDijkstraActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscarDijkstra);
        btnBuscarDijkstra.setBounds(210, 430, 161, 22);

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Conexion de Vertices:");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(600, 130, 180, 17);

        textPraBBP.setBackground(new java.awt.Color(255, 153, 102));
        textPraBBP.setText(" ");

        textBPA.setBackground(new java.awt.Color(255, 204, 102));
        textBPA.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(textPraBBP, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(textBPA, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(textBPA, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(textPraBBP, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2);
        jPanel2.setBounds(20, 280, 460, 60);

        btnBuscarFloyd.setBackground(new java.awt.Color(51, 153, 255));
        btnBuscarFloyd.setForeground(new java.awt.Color(0, 0, 0));
        btnBuscarFloyd.setText("Buscar Camino");
        btnBuscarFloyd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarFloydActionPerformed(evt);
            }
        });
        jPanel1.add(btnBuscarFloyd);
        btnBuscarFloyd.setBounds(630, 430, 161, 22);

        jLabel11.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("Floyd:");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(630, 400, 102, 20);

        ListaFloyd.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(ListaFloyd);

        jPanel1.add(jScrollPane4);
        jScrollPane4.setBounds(630, 470, 230, 170);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 1150, 710);

        setSize(new java.awt.Dimension(935, 696));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnMostrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMostrarActionPerformed
        FrmGrafoVista frm = new FrmGrafoVista(grafoD);
        frm.setSize(600, 450);
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
    }//GEN-LAST:event_btnMostrarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        try
        {
            this.generarGrafo();
        } catch (Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error en grafos", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void tablaDijkstraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDijkstraMouseClicked

    }//GEN-LAST:event_tablaDijkstraMouseClicked

    private void btnOk1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOk1ActionPerformed
        try
        {
            adyacencias();
        } catch (Exception ex)
        {
            JOptionPane.showMessageDialog(null, "Error datos incorrectos", "ERROR", JOptionPane.ERROR_MESSAGE);
            this.peso.setText(null);
        }
        jPanel2.setVisible(true);

    }//GEN-LAST:event_btnOk1ActionPerformed

    private void btn_BuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BuscarActionPerformed
        caminoDijsktra();
    }//GEN-LAST:event_btn_BuscarActionPerformed

    private void btnBuscarDijkstraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarDijkstraActionPerformed

        try
        {
            this.tablaDijkstra.setModel(this.getTableListaDis(true));
        } catch (Exception ex)
        {
            Logger.getLogger(FrmGrafo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnBuscarDijkstraActionPerformed

    private void cbxinicioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxinicioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxinicioActionPerformed

    private void btnBuscarFloydActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarFloydActionPerformed
        caminoFloyd();
    }//GEN-LAST:event_btnBuscarFloydActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(FrmGrafo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(FrmGrafo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(FrmGrafo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(FrmGrafo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try
                {
                    new FrmGrafo().setVisible(true);
                } catch (Exception ex)
                {
                    Logger.getLogger(FrmGrafo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> ListaFloyd;
    private javax.swing.ButtonGroup TipoGrafo;
    private javax.swing.JButton btnBuscarDijkstra;
    private javax.swing.JButton btnBuscarFloyd;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnMostrar;
    private javax.swing.JButton btnOk1;
    private javax.swing.JButton btn_Buscar;
    private javax.swing.JComboBox<String> cbxDestino;
    private javax.swing.JComboBox<String> cbxFin;
    private javax.swing.JComboBox<String> cbxOrigen1;
    private javax.swing.JComboBox<String> cbxinicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField nombreVertice;
    private javax.swing.JTextField peso;
    private javax.swing.JTable tablaDijkstra;
    private javax.swing.JTable tblTabla;
    private javax.swing.JLabel textBPA;
    private javax.swing.JLabel textPraBBP;
    // End of variables declaration//GEN-END:variables
}
