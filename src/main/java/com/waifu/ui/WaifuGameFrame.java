package com.waifu.ui;

import com.waifu.facade.AnimeGameFacade;
import com.waifu.model.Waifu;
import com.waifu.strategy.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

/** Interfaz completa del juego: inicio, selección, combate, guardado y resultado. */
public class WaifuGameFrame extends JFrame {
    private static final Color BG = new Color(7, 11, 28), PANEL = new Color(15,22,45), PANEL2 = new Color(24,31,60);
    private static final Color TEXT = new Color(239,243,255), MUTED = new Color(159,171,207), PINK = new Color(235,64,191), CYAN = new Color(57,211,255), GREEN = new Color(44,205,145), RED = new Color(244,85,100);

    private final AnimeGameFacade facade;
    private final List<Waifu> allWaifus;
    private final ImageRepository images = new ImageRepository();
    private final BattleSaveManager saveManager = new BattleSaveManager();
    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);

    private List<Waifu> allies = new ArrayList<>();
    private List<Waifu> enemies = new ArrayList<>();
    private int turn = 1, enemyTurnIndex = 0, selectedAllyId = -1, selectedEnemyId = -1;
    private String strategyName = "EQUILIBRADA";
    private boolean battleOver = false;
    private final List<String> combatLog = new ArrayList<>();
    private final Map<Integer,Integer> damageDealt = new HashMap<>();

    private JPanel selectionPanel, battlePanel;
    private JLabel battleStatus, turnLabel;
    private JTextArea logArea;
    private JComboBox<String> strategyBox;
    private JComboBox<Waifu> allyBox, enemyBox;
    private JPanel allyCards, enemyCards;

    public WaifuGameFrame(AnimeGameFacade facade, List<Waifu> waifus) {
        this.facade = facade;
        this.allWaifus = List.copyOf(waifus);
        configureWindow();
        buildScreens();
        showHome();
    }

    private void configureWindow() {
        setTitle("Waifu Nexus — OO Design Lab");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1180,760));
        setSize(1450,900);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
    }

    private void buildScreens() {
        root.setBackground(BG);
        root.add(homeScreen(), "HOME");
        root.add(selectionScreen(), "SELECT");
        root.add(combatScreen(), "BATTLE");
        setContentPane(root);
    }

    private JPanel base(String title, String subtitle) {
        JPanel p = new JPanel(new BorderLayout(12,12)); p.setBackground(BG); p.setBorder(new EmptyBorder(18,18,18,18));
        JPanel head = panel(new BorderLayout()); head.setBorder(new EmptyBorder(14,18,14,18));
        JLabel t = new JLabel(title); t.setForeground(TEXT); t.setFont(new Font("SansSerif",Font.BOLD,28));
        JLabel s = new JLabel(subtitle); s.setForeground(MUTED); s.setFont(new Font("SansSerif",Font.PLAIN,13));
        JPanel h = new JPanel(); h.setOpaque(false); h.setLayout(new BoxLayout(h,BoxLayout.Y_AXIS)); h.add(t); h.add(Box.createVerticalStrut(3)); h.add(s);
        head.add(h,BorderLayout.WEST); p.add(head,BorderLayout.NORTH); return p;
    }

    private JPanel homeScreen() {
        JPanel p = base("🌸 WAIFU NEXUS", "OO Design Lab • Elegí un modo de juego");
        JPanel center = new JPanel(new GridBagLayout()); center.setOpaque(false);
        JPanel box = panel(new GridLayout(4,1,12,12)); box.setBorder(new EmptyBorder(25,35,25,35));
        JLabel title = new JLabel("¿Qué deseas hacer?", SwingConstants.CENTER); title.setForeground(TEXT); title.setFont(new Font("SansSerif",Font.BOLD,22)); box.add(title);
        JButton newBtn = button("⚔  NUEVO COMBATE", PINK); newBtn.addActionListener(e -> showSelection()); box.add(newBtn);
        JButton continueBtn = button("↻  CONTINUAR COMBATE ANTERIOR", new Color(61,100,178)); continueBtn.setEnabled(saveManager.exists());
        continueBtn.addActionListener(e -> continueSavedBattle()); box.add(continueBtn);
        JLabel info = new JLabel("Guardado automático después de cada turno • 1 a 3 aliados y 1 a 3 enemigos", SwingConstants.CENTER); info.setForeground(CYAN); info.setFont(new Font("SansSerif",Font.PLAIN,12)); box.add(info);
        center.add(box); p.add(center,BorderLayout.CENTER); p.add(footer(),BorderLayout.SOUTH); return p;
    }

    private JPanel selectionScreen() {
        selectionPanel = base("⚔ NUEVO COMBATE", "Seleccioná entre 1 y 3 waifus para cada equipo");
        JPanel content = new JPanel(new GridLayout(1,2,14,0)); content.setOpaque(false);
        content.add(teamSelector("TU EQUIPO", true)); content.add(teamSelector("EQUIPO ENEMIGO", false));
        selectionPanel.add(content,BorderLayout.CENTER);
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER,12,8)); bottom.setOpaque(false);
        JButton back = button("← VOLVER", new Color(70,80,115)); back.addActionListener(e -> showHome());
        JButton start = button("▶ COMENZAR COMBATE", PINK); start.addActionListener(e -> startNewBattle());
        bottom.add(back); bottom.add(start); selectionPanel.add(bottom,BorderLayout.SOUTH);
        return selectionPanel;
    }

    private JPanel teamSelector(String title, boolean alliesSelector) {
        JPanel outer = panel(new BorderLayout(8,8));
        JLabel head = section(title + "  •  1–3 SELECCIONADAS"); outer.add(head,BorderLayout.NORTH);
        JPanel list = new JPanel(); list.setOpaque(false); list.setLayout(new BoxLayout(list,BoxLayout.Y_AXIS));
        ButtonGroup group = new ButtonGroup();
        for (Waifu w : allWaifus) {
            JCheckBox check = new JCheckBox(); check.setOpaque(false); check.setSelected(false);
            check.setActionCommand(String.valueOf(w.getId()));
            check.addActionListener(e -> {
                int count = 0;
                for (Component c : list.getComponents()) if (c instanceof JPanel row) {
                    JCheckBox cb = (JCheckBox) row.getClientProperty("check"); if (cb != null && cb.isSelected()) count++;
                }
                if (count > 3) check.setSelected(false);
            });
            JPanel row = new JPanel(new BorderLayout(8,5)); row.setBackground(PANEL2); row.setBorder(new EmptyBorder(7,7,7,7)); row.setMaximumSize(new Dimension(Integer.MAX_VALUE,86));
            row.putClientProperty("check", check);
            row.add(check,BorderLayout.WEST); row.add(new JLabel(images.avatar(w,58,58)),BorderLayout.CENTER);
            JPanel info = new JPanel(); info.setOpaque(false); info.setLayout(new BoxLayout(info,BoxLayout.Y_AXIS));
            JLabel n = new JLabel(w.getName()+" • "+w.getElement()); n.setForeground(TEXT); n.setFont(new Font("SansSerif",Font.BOLD,14));
            JLabel s = new JLabel("ATK "+w.getAttack()+"  DEF "+w.getDefense()+"  HP "+w.getMaxHp()); s.setForeground(CYAN); s.setFont(new Font("Monospaced",Font.BOLD,10));
            info.add(n); info.add(s); row.add(info,BorderLayout.EAST); list.add(row); list.add(Box.createVerticalStrut(6));
        }
        JScrollPane scroll = new JScrollPane(list); scroll.setBorder(null); scroll.getViewport().setOpaque(false); scroll.setOpaque(false); outer.add(scroll,BorderLayout.CENTER);
        outer.putClientProperty("list", list); outer.putClientProperty("selector", alliesSelector);
        return outer;
    }

    private JPanel combatScreen() {
        battlePanel = base("🌸 COMBATE", "Batalla por turnos • el combate se guarda automáticamente");
        JPanel center = new JPanel(new BorderLayout(10,10)); center.setOpaque(false);
        center.add(teamArea(),BorderLayout.CENTER); center.add(logPanel(),BorderLayout.EAST);
        battlePanel.add(center,BorderLayout.CENTER); battlePanel.add(actionBar(),BorderLayout.SOUTH); return battlePanel;
    }

    private JPanel teamArea() {
        JPanel arena = panel(new BorderLayout(8,8));
        JPanel top = new JPanel(new BorderLayout()); top.setOpaque(false);
        turnLabel = new JLabel("TURNO 1",SwingConstants.CENTER); turnLabel.setForeground(TEXT); turnLabel.setFont(new Font("SansSerif",Font.BOLD,18));
        battleStatus = new JLabel("Seleccioná tu atacante y el objetivo",SwingConstants.CENTER); battleStatus.setForeground(MUTED); top.add(turnLabel,BorderLayout.NORTH); top.add(battleStatus,BorderLayout.SOUTH); arena.add(top,BorderLayout.NORTH);
        JPanel fighters = new JPanel(new GridLayout(1,3,10,10)); fighters.setOpaque(false);
        JPanel ally = panel(new BorderLayout()); ally.add(section("TU EQUIPO — VIVOS"),BorderLayout.NORTH); allyCards = new JPanel(new GridLayout(0,1,6,6)); allyCards.setOpaque(false); ally.add(allyCards,BorderLayout.CENTER);
        JPanel versus = panel(new GridBagLayout()); JLabel vs = new JLabel("VS",SwingConstants.CENTER); vs.setForeground(PINK); vs.setFont(new Font("SansSerif",Font.BOLD,32)); versus.add(vs);
        JPanel enemy = panel(new BorderLayout()); enemy.add(section("ENEMIGAS — VIVAS"),BorderLayout.NORTH); enemyCards = new JPanel(new GridLayout(0,1,6,6)); enemyCards.setOpaque(false); enemy.add(enemyCards,BorderLayout.CENTER);
        fighters.add(ally); fighters.add(versus); fighters.add(enemy); arena.add(fighters,BorderLayout.CENTER);
        return arena;
    }

    private JPanel actionBar() {
        JPanel p = panel(new FlowLayout(FlowLayout.CENTER,10,8));
        allyBox = new JComboBox<>(); enemyBox = new JComboBox<>(); strategyBox = new JComboBox<>(new String[]{"AGRESIVA","EQUILIBRADA","DEFENSIVA"});
        styleCombo(allyBox); styleCombo(enemyBox); styleCombo(strategyBox);
        allyBox.addActionListener(e -> { if (allyBox.getSelectedItem()!=null) selectedAllyId=((Waifu)allyBox.getSelectedItem()).getId(); });
        enemyBox.addActionListener(e -> { if (enemyBox.getSelectedItem()!=null) selectedEnemyId=((Waifu)enemyBox.getSelectedItem()).getId(); });
        strategyBox.addActionListener(e -> { if(strategyBox.getSelectedItem()!=null) strategyName=(String)strategyBox.getSelectedItem(); });
        p.add(label("ATACANTE")); p.add(allyBox); p.add(label("OBJETIVO")); p.add(enemyBox); p.add(label("ESTRATEGIA")); p.add(strategyBox);
        JButton attack=button("⚔ ATACAR",PINK); attack.addActionListener(this::attack); p.add(attack);
        JButton heal=button("💚 CURAR",GREEN); heal.addActionListener(e->heal()); p.add(heal);
        JButton save=button("💾 GUARDAR",new Color(65,103,176)); save.addActionListener(e->saveBattle()); p.add(save);
        JButton home=button("⌂ INICIO",new Color(70,80,115)); home.addActionListener(e->{saveBattle();showHome();}); p.add(home);
        return p;
    }

    private JPanel logPanel() {
        JPanel p=panel(new BorderLayout(8,8)); p.setPreferredSize(new Dimension(330,0)); p.add(section("📜 REGISTRO"),BorderLayout.NORTH);
        logArea=new JTextArea(); logArea.setEditable(false); logArea.setLineWrap(true); logArea.setWrapStyleWord(true); logArea.setBackground(new Color(7,11,24)); logArea.setForeground(TEXT); logArea.setFont(new Font("Monospaced",Font.PLAIN,11)); logArea.setBorder(new EmptyBorder(8,8,8,8));
        p.add(new JScrollPane(logArea),BorderLayout.CENTER); return p;
    }

    private void rebuildScreens(){
        root.removeAll();
        root.add(homeScreen(),"HOME");
        root.add(selectionScreen(),"SELECT");
        root.add(combatScreen(),"BATTLE");
        root.revalidate();
        root.repaint();
    }

    private void showHome(){ rebuildScreens(); cards.show(root,"HOME"); }

    private void showSelection(){ rebuildScreens(); cards.show(root,"SELECT"); }

    private void startNewBattle(){
        List<Waifu> selectedAllies=selectedFromPanel(0); List<Waifu> selectedEnemies=selectedFromPanel(1);
        boolean overlap = selectedAllies.stream().anyMatch(selectedEnemies::contains);
        if(overlap){
            JOptionPane.showMessageDialog(this,"Una misma waifu no puede pertenecer a ambos equipos.","Selección inválida",JOptionPane.WARNING_MESSAGE);
            return;
        }
        if(selectedAllies.isEmpty()||selectedAllies.size()>3||selectedEnemies.isEmpty()||selectedEnemies.size()>3){
            JOptionPane.showMessageDialog(this,"Debés seleccionar entre 1 y 3 waifus en cada equipo.","Selección inválida",JOptionPane.WARNING_MESSAGE); return;
        }
        saveManager.delete(); for(Waifu w:allWaifus) w.resetBattleState();
        allies=new ArrayList<>(selectedAllies); enemies=new ArrayList<>(selectedEnemies); turn=1; enemyTurnIndex=0; battleOver=false; damageDealt.clear(); combatLog.clear();
        selectedAllyId=allies.get(0).getId(); selectedEnemyId=enemies.get(0).getId(); strategyName="EQUILIBRADA";
        addLog("⚔ Nuevo combate: "+allies.size()+" aliadas vs "+enemies.size()+" enemigas.");
        showBattle(); saveBattle();
    }

    private List<Waifu> selectedFromPanel(int index){
        JPanel outer=(JPanel)selectionPanel.getComponent(1); JPanel team=(JPanel)outer.getComponent(index); JPanel list=(JPanel)team.getClientProperty("list");
        List<Waifu> result=new ArrayList<>();
        for(Component c:list.getComponents()) if(c instanceof JPanel row){ JCheckBox cb=(JCheckBox)row.getClientProperty("check"); if(cb!=null&&cb.isSelected()) { int id=Integer.parseInt(cb.getActionCommand()); allWaifus.stream().filter(w->w.getId()==id).findFirst().ifPresent(result::add); } }
        return result;
    }

    private void continueSavedBattle(){
        BattleSaveManager.SavedBattle saved=saveManager.load(allWaifus);
        if(saved==null){ JOptionPane.showMessageDialog(this,"No hay un combate guardado."); return; }
        allies=new ArrayList<>(saved.allies()); enemies=new ArrayList<>(saved.enemies()); turn=saved.turn(); enemyTurnIndex=saved.enemyTurnIndex(); selectedAllyId=saved.selectedAllyId(); selectedEnemyId=saved.selectedEnemyId(); strategyName=saved.strategy(); combatLog.clear(); combatLog.addAll(saved.log()); damageDealt.clear(); damageDealt.putAll(saved.damageDealt()); battleOver=false;
        showBattle(); addLog("↻ Combate anterior cargado. Continuás exactamente desde el turno "+turn+".");
    }

    private void showBattle(){ cards.show(root,"BATTLE"); refreshBattle(); }

    private void attack(ActionEvent e){
        if(battleOver) return; Waifu a=getById(allies,selectedAllyId), d=getById(enemies,selectedEnemyId);
        if(a==null||d==null||a.isKnockedOut()||d.isKnockedOut()){refreshBattle();return;}
        CombatStrategy s=strategy(); int dmg=facade.fight(a,d,s); damageDealt.merge(a.getId(),Math.max(0,dmg),Integer::sum);
        addLog("⚔ TU TURNO — "+a.getName()+" usa "+s.name()+" y causa "+dmg+" de daño a "+d.getName()+".");
        if(d.isKnockedOut()) addLog("☠ "+d.getName()+" desaparece del campo de batalla.");
        if(checkEnd()) return;
        saveBattle();
        new javax.swing.Timer(500,ev->{((javax.swing.Timer)ev.getSource()).stop(); enemyAttack();}).start();
    }

    private void enemyAttack(){
        if(battleOver)return; Waifu enemy=nextAliveEnemy(), target=getById(allies,selectedAllyId); if(target==null||target.isKnockedOut())target=firstAlive(allies);
        if(enemy==null||target==null){checkEnd();return;}
        CombatStrategy s=switch(turn%3){case 0->new AggressiveStrategy();case 1->new BalancedStrategy();default->new DefensiveStrategy();};
        int dmg=facade.fight(enemy,target,s); damageDealt.merge(enemy.getId(),Math.max(0,dmg),Integer::sum);
        addLog("👿 TURNO ENEMIGO — "+enemy.getName()+" usa "+s.name()+" y causa "+dmg+" de daño a "+target.getName()+".");
        if(target.isKnockedOut()) addLog("☠ "+target.getName()+" desaparece del campo de batalla.");
        turn++; if(firstAlive(allies)!=null) selectedAllyId=firstAlive(allies).getId(); if(firstAlive(enemies)!=null) selectedEnemyId=firstAlive(enemies).getId();
        if(checkEnd())return; saveBattle(); refreshBattle();
    }

    private void heal(){
        if(battleOver)return; Waifu a=getById(allies,selectedAllyId); if(a==null||a.isKnockedOut())return; a.heal(35); addLog("💚 "+a.getName()+" recupera 35 HP.");
        if(checkEnd())return; saveBattle(); new javax.swing.Timer(450,ev->{((javax.swing.Timer)ev.getSource()).stop();enemyAttack();}).start();
    }

    private boolean checkEnd(){
        if(alliesDead()){ finishBattle(false); return true; }
        if(enemiesDead()){ finishBattle(true); return true; }
        refreshBattle(); return false;
    }
    private boolean all(boolean value){return value;} // mantiene legible la condición de fin
    private boolean alliesDead(){return allies.isEmpty()||allies.stream().allMatch(Waifu::isKnockedOut);}
    private boolean enemiesDead(){return enemies.isEmpty()||enemies.stream().allMatch(Waifu::isKnockedOut);}

    private void finishBattle(boolean victory){
        battleOver=true; saveManager.delete(); refreshBattle(); showResult(victory);
    }

    private void showResult(boolean victory){
        JPanel result=base(victory?"🏆 VICTORIA":"💀 DERROTA", victory?"Todas las enemigas fueron derrotadas":"Tu equipo fue derrotado");
        JPanel center=new JPanel(new BorderLayout(10,10)); center.setOpaque(false);
        JLabel headline=new JLabel(victory?"WAIFUS GANADORAS":"WAIFUS QUE PERDIERON",SwingConstants.CENTER); headline.setForeground(victory?GREEN:RED); headline.setFont(new Font("SansSerif",Font.BOLD,25)); center.add(headline,BorderLayout.NORTH);
        List<Waifu> displayedTeam=allies;
        JPanel resultCards=new JPanel(new GridLayout(1,Math.max(1,displayedTeam.size()),10,10)); resultCards.setOpaque(false);
        for(Waifu w:displayedTeam) resultCards.add(resultCard(w,victory)); center.add(resultCards,BorderLayout.CENTER);
        JLabel score=new JLabel("PUNTAJE FINAL DEL EQUIPO: "+teamScore(displayedTeam),SwingConstants.CENTER); score.setForeground(CYAN); score.setFont(new Font("SansSerif",Font.BOLD,22)); center.add(score,BorderLayout.SOUTH);
        result.add(center,BorderLayout.CENTER);
        JPanel bottom=new JPanel(new FlowLayout(FlowLayout.CENTER,12,8)); bottom.setOpaque(false); JButton again=button("🏠 VOLVER A JUGAR",PINK); again.addActionListener(e->showHome()); bottom.add(again); result.add(bottom,BorderLayout.SOUTH);
        root.add(result,"RESULT"); cards.show(root,"RESULT"); root.revalidate(); root.repaint();
    }

    private JPanel resultCard(Waifu w,boolean winner){
        JPanel p=panel(new BorderLayout(6,6)); p.add(new JLabel(images.avatar(w,170,180)),BorderLayout.CENTER); JLabel n=new JLabel(w.getName(),SwingConstants.CENTER); n.setForeground(TEXT); n.setFont(new Font("SansSerif",Font.BOLD,18));
        int score=individualScore(w); JLabel s=new JLabel("Puntaje: "+score+"  •  HP: "+w.getHp()+"/"+w.getMaxHp(),SwingConstants.CENTER); s.setForeground(winner?GREEN:RED); p.add(n,BorderLayout.NORTH); p.add(s,BorderLayout.SOUTH); return p;
    }
    private int individualScore(Waifu w){return Math.max(0,w.getHp())+damageDealt.getOrDefault(w.getId(),0)*2;}
    private int teamScore(List<Waifu> team){return team.stream().mapToInt(this::individualScore).sum();}

    private void refreshBattle(){
        if(allyCards==null)return; allyCards.removeAll(); enemyCards.removeAll();
        for(Waifu w:allies) if(!w.isKnockedOut()) allyCards.add(combatCard(w,true));
        for(Waifu w:enemies) if(!w.isKnockedOut()) enemyCards.add(combatCard(w,false));
        populateCombo(allyBox,allies,selectedAllyId); populateCombo(enemyBox,enemies,selectedEnemyId); if(strategyBox!=null)strategyBox.setSelectedItem(strategyName);
        turnLabel.setText("TURNO "+turn); battleStatus.setText(battleOver?"COMBATE FINALIZADO":"Las eliminadas desaparecen del campo y no pueden volver a seleccionarse."); logArea.setText(String.join("\n",combatLog)); logArea.setCaretPosition(logArea.getDocument().getLength());
        allyCards.revalidate(); enemyCards.revalidate(); allyCards.repaint(); enemyCards.repaint();
    }

    private JPanel combatCard(Waifu w,boolean ally){
        JPanel p=panel(new BorderLayout(6,4)); p.setBorder(BorderFactory.createLineBorder(ally?CYAN:PINK,1)); p.add(new JLabel(images.avatar(w,100,115)),BorderLayout.WEST);
        JPanel info=new JPanel();info.setOpaque(false);info.setLayout(new BoxLayout(info,BoxLayout.Y_AXIS)); JLabel n=new JLabel(w.getName());n.setForeground(TEXT);n.setFont(new Font("SansSerif",Font.BOLD,15));
        JLabel st=new JLabel("ATK "+w.getAttack()+" DEF "+w.getDefense());st.setForeground(CYAN);st.setFont(new Font("Monospaced",Font.BOLD,10)); JProgressBar hp=progress();hp.setMaximum(w.getMaxHp());hp.setValue(w.getHp());hp.setString(w.getHp()+" / "+w.getMaxHp());
        JLabel state=new JLabel(w.getStateName());state.setForeground(w.getStateName().equals("HERIDA")?new Color(255,190,65):GREEN); info.add(n);info.add(st);info.add(hp);info.add(state);p.add(info,BorderLayout.CENTER);return p;
    }

    private void populateCombo(JComboBox<Waifu> box,List<Waifu> team,int selected){ if(box==null)return; box.removeAllItems(); for(Waifu w:team)if(!w.isKnockedOut())box.addItem(w); for(int i=0;i<box.getItemCount();i++)if(box.getItemAt(i).getId()==selected){box.setSelectedIndex(i);return;} if(box.getItemCount()>0)box.setSelectedIndex(0); }
    private Waifu getById(List<Waifu> team,int id){return team.stream().filter(w->w.getId()==id&&!w.isKnockedOut()).findFirst().orElse(null);}
    private Waifu firstAlive(List<Waifu> team){return team.stream().filter(w->!w.isKnockedOut()).findFirst().orElse(null);}
    private Waifu nextAliveEnemy(){if(enemies.isEmpty())return null;for(int i=0;i<enemies.size();i++){int idx=(enemyTurnIndex+i)%enemies.size();Waifu w=enemies.get(idx);if(!w.isKnockedOut()){enemyTurnIndex=(idx+1)%enemies.size();return w;}}return null;}
    private CombatStrategy strategy(){return switch(strategyName){case "AGRESIVA"->new AggressiveStrategy();case "DEFENSIVA"->new DefensiveStrategy();default->new BalancedStrategy();};}

    private void saveBattle(){if(allies.isEmpty()||enemies.isEmpty()||battleOver)return; saveManager.save(allWaifus,allies,enemies,turn,enemyTurnIndex,selectedAllyId,selectedEnemyId,strategyName,combatLog,damageDealt);}
    private void addLog(String s){combatLog.add(s);if(logArea!=null){logArea.append((logArea.getText().isEmpty()?"":"\n")+s);logArea.setCaretPosition(logArea.getDocument().getLength());}}

    private JPanel resultFooter(){return footer();}
    private JPanel footer(){JPanel p=new JPanel(new BorderLayout());p.setOpaque(false);JLabel a=new JLabel("Waifu Nexus • Java 17 • Swing");a.setForeground(MUTED);JLabel b=new JLabel("More Waifus, More Power 🌸");b.setForeground(PINK);p.add(a,BorderLayout.WEST);p.add(b,BorderLayout.EAST);return p;}
    private JPanel panel(LayoutManager l){JPanel p=new JPanel(l);p.setBackground(PANEL);p.setBorder(new EmptyBorder(10,10,10,10));return p;}
    private JLabel section(String s){JLabel l=new JLabel(s);l.setForeground(TEXT);l.setFont(new Font("SansSerif",Font.BOLD,13));return l;}
    private JLabel label(String s){JLabel l=new JLabel(s);l.setForeground(MUTED);l.setFont(new Font("SansSerif",Font.BOLD,11));return l;}
    private JButton button(String s,Color c){JButton b=new JButton(s);b.setForeground(Color.WHITE);b.setBackground(c);b.setFocusPainted(false);b.setBorder(new EmptyBorder(10,16,10,16));b.setFont(new Font("SansSerif",Font.BOLD,12));return b;}
    private void styleCombo(JComboBox<?> b){b.setBackground(PANEL2);b.setForeground(TEXT);b.setPreferredSize(new Dimension(145,31));b.setFocusable(false);}
    private JProgressBar progress(){JProgressBar p=new JProgressBar();p.setStringPainted(true);p.setForeground(GREEN);p.setBackground(new Color(43,51,78));p.setBorderPainted(false);p.setPreferredSize(new Dimension(180,18));return p;}
}
