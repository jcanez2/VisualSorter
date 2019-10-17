import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class VisualSorting {
    //FRAME
    private JFrame jFrame;
    //GENERAL VARIABLES
    private int len = 50;
    private int off = 0;
    private int curAlg = 0;
    private int spd = 15;
    private int compare = 0;
    private int acc = 0;
    //GRAPH VARIABLES
    private final int SIZE = 600;
    private int current = -1;
    private int check = -1;
    private int width = SIZE/len;
    private int type = 0;
    //ARRAYS
    private int[] list;
    private String[] algorithms = {"Selection Sort",
            "Bubble Sort",
            "Insertion Sort",
            "Jesus Sort",
            "Quick Sort",
            "Merge Sort",
            };

    //BOOLEANS
    private boolean sorting = false;
    private boolean mixed = true;
    //UTIL OBJECTS
    SortingAlgorithms algorithm = new SortingAlgorithms();
    Random r = new Random();
    //PANELS
    JPanel tools = new JPanel();
    GraphCanvas canvas;
    //LABELS
    JLabel algorithmL = new JLabel("Sorting Types");
    JComboBox alg = new JComboBox(algorithms);

    //BUTTONS
    JButton sort = new JButton("Sort");
    JButton mix = new JButton("Mix");
    //JButton Pause = new JButton("Pause");
    //JButton Play = new JButton("Play");
    //SLIDERS
    JSlider size = new JSlider(JSlider.HORIZONTAL,1,6,1);
    JSlider speed = new JSlider(JSlider.HORIZONTAL,0,100,spd);
    //BORDER STYLE
    Border bottomeBound = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    //CONSTRUCTOR
    public VisualSorting() {
        mixList();	//CREATE THE LIST
        initialize();	//INITIALIZE THE GUI
    }

    public void createList() {
        list = new int[len];	//CREATES A LIST EQUAL TO THE LENGTH
        for(int i = 0; i < len; i++) {	//FILLS THE LIST FROM 1-LEN
            list[i] = i+1;
        }
    }

    public void mixList() {
        createList();
        for(int j = 0; j < 400; j++) {	    //MIX RUNS 500 TIMES
            for(int i = 0; i < len; i++) {	//ACCESS EACH ELEMENT OF THE LIST
                int rand = r.nextInt(len);	//PICK A RANDOM NUM FROM 0-LEN
                int temp = list[i];			//SETS TEMP INT TO CURRENT ELEMENT
                list[i] = list[rand];		//SWAPS THE CURRENT INDEX WITH RANDOM INDEX
                list[rand] = temp;			//SETS THE RANDOM INDEX TO THE TEMP
            }
        }
        sorting = false;
        mixed = true;
    }

    public void initialize() {
        //SET UP FRAME
        jFrame = new JFrame();
        jFrame.setSize(815,645);
        jFrame.setTitle("Sorting Types");
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        jFrame.getContentPane().setLayout(null);

        //SET UP TOOLBAR
        tools.setLayout(null);
        tools.setBounds(5,10,180,590);
        tools.setBorder(BorderFactory.createTitledBorder(bottomeBound,"Inputs"));
        tools.setBackground(Color.GRAY);

        //SET UP ALGORITHM LABEL
        algorithmL.setHorizontalAlignment(JLabel.CENTER);
        algorithmL.setBounds(40,20,100,25);
        tools.add(algorithmL);

        //SET UP DROP DOWN
        alg.setBounds(30,45,120,25);
        tools.add(alg);

        //SET UP SORT BUTTON
        sort.setBounds(40,540,100,25);
        tools.add(sort);

        //SET UP MIX BUTTON
        mix.setBounds(40,500,100,25);
        tools.add(mix);

        //SET UP CANVAS FOR GRAPH
        canvas = new GraphCanvas();
        canvas.setBounds(190,0,SIZE,SIZE);
        canvas.setBorder(BorderFactory.createLineBorder(Color.black));
        jFrame.getContentPane().add(tools);
        jFrame.getContentPane().add(canvas);
        jFrame.getContentPane().setBackground(Color.DARK_GRAY);

        //ADD ACTION LISTENERS
        alg.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                curAlg = alg.getSelectedIndex();
            }

        });
        sort.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mixed) {
                    sorting = true;
                    compare = 0;
                    acc = 0;
                }
            }
        });
        mix.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mixList();
                reset();
            }
        });
        speed.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent arg0) {
                spd = (int)speed.getValue();
            }
        });
        size.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int val = size.getValue();
                if(size.getValue() == 5)
                    val = 4;
                len = val * 50;
                if(list.length != len)
                    mixList();
                reset();
            }
        });
        sorting();
    }

    //SORTING STATE
    public void sorting() {
        if(sorting) {
            try {
                switch(curAlg) {	//CURRENT ALGORITHM IS EXECUTED
                    case 0:
                        algorithm.selectionSort();
                        break;
                    case 1:
                        algorithm.bubbleSort();
                        break;
                    case 2:
                        algorithm.insertionSort(0, len-1);
                        break;
                    case 3:
                        algorithm.jesusSort(len);
                        break;
                    case 4:
                        algorithm.quickSort(0,len-1);
                        break;
                    case 5:
                        algorithm.mergeSort(0,len-1);
                        break;
                }
            } catch(IndexOutOfBoundsException e) {}	//EXCEPTION HANDLER IN CASE LIST ACCESS IS OUT OF BOUNDS
        }
        reset();	//RESET
        pause();	//GO INTO PAUSE STATE
    }

    //PAUSE STATE
    public void pause() {
        int i = 0;
        while(!sorting) {	//LOOP RUNS UNTIL SORTING STARTS
            i++;
            if(i > 100)
                i = 0;
            try {
                Thread.sleep(1);
            } catch(Exception e) {}
        }
        sorting();	//EXIT THE LOOP AND START SORTING
    }

    //RESET SOME VARIABLES
    public void reset() {
        sorting = false;
        current = -1;
        check = -1;
        off = 0;
        Update();
    }

    //DELAY METHOD
    public void delay() {
        try {
            Thread.sleep(spd);
        } catch(Exception e) {}
    }

    //UPDATES THE GRAPH AND LABELS
    public void Update() {
        width = SIZE/len;
        canvas.repaint();
    }

    //MAIN METHOD
    public static void main(String[] args) {
        new VisualSorting();
    }

    //SUB CLASS TO CONTROL THE GRAPH
    class GraphCanvas extends JPanel {

        public GraphCanvas() {
            setBackground(Color.black);
        }

        //PAINTS THE GRAPH
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            for(int i = 0; i < len; i++) {	//RUNS TROUGH EACH ELEMENT OF THE LIST
                int HEIGHT = list[i]*width;	//SETS THE HEIGHT OF THE ELEMENT ON THE GRAPH
                if(type == 0) {		//BAR GRAPH TYPE
                    g.setColor(Color.green);	//DEFAULT COLOR
                    if(current > -1 && i == current) {
                        g.setColor(Color.red);	//COLOR OF CURRENT
                    }
                    if(check > -1 && i == check) {
                        g.setColor(Color.blue);	//COLOR OF CHECKING
                    }
                    //DRAWS THE BAR AND THE BLACK OUTLINE
                    g.fillRect(i*width, SIZE-HEIGHT, width, HEIGHT);
                    g.setColor(Color.BLUE);
                    g.drawRect(i*width, SIZE-HEIGHT, width, HEIGHT);
                }
                else if(type == 1) {	//DOT PLOT TYPE
                    g.setColor(Color.white);	//DEFAULT COLOR
                    if(current > -1 && i == current) {
                        g.setColor(Color.white);	//COLOR OF CURRENT
                    }
                    if(check > -1 && i == check) {
                        g.setColor(Color.orange);	//COLOR OF CHECKING
                    }
                    //DRAWS THE DOT
                    g.fillOval(i*width,SIZE-HEIGHT,width,width);
                }
            }
        }
    }

    /************************************************Sorting Methods****************************************************/
    class SortingAlgorithms {
        public void selectionSort() {
            int c = 0;
            while(c < len && sorting) {
                int sm = c;
                current = c;
                for(int i = c+1; i < len; i++) {
                    if(!sorting)
                        break;
                    if(list[i] < list[sm]) {
                        sm = i;
                    }
                    check = i;
                    compare++;	acc+=2;
                    Update();
                    delay();
                }
                if(c != sm)
                    swap(c,sm);
                c++;
            }
        }
        public void insertionSort(int start, int end) {
            for(int i = start+1; i <= end; i++) {
                current = i;
                int j = i;
                while(list[j] < list[j-1] && sorting) {
                    swap(j,j-1);
                    check = j;
                    compare++;	acc+=2;
                    Update();
                    delay();
                    if(j > start+1)
                        j--;
                }
            }
        }
        public void bubbleSort() {
            int c = 1;
            boolean noswaps = false;
            while(!noswaps && sorting) {
                current = len-c;
                noswaps = true;
                for(int i = 0; i < len-c; i++) {
                    if(!sorting)
                        break;
                    if(list[i+1] < list[i]) {
                        noswaps = false;
                        swap(i,i+1);
                    }
                    check = i+1;
                    compare++;	acc+=2;
                    Update();
                    delay();
                }
                c++;
            }
        }
        public void quickSort(int lo, int hi) {
            if(!sorting)
                return;
            current = hi;
            if(lo < hi) {
                int p = partition(lo,hi);
                quickSort(lo,p-1);
                quickSort(p+1, hi);
            }
        }
        public int partition(int lo, int hi) {
            int pivot = list[hi];	acc++;
            int i = lo - 1;
            for(int j = lo; j < hi; j++) {
                check = j;
                if(!sorting)
                    break;
                if(list[j] < pivot) {
                    i++;
                    swap(i,j);
                }
                compare++;	acc++;
                Update();
                delay();
            }
            swap(i+1,hi);
            Update();
            delay();
            return i+1;
        }
        void merge(int l, int m, int r)
        {
            int n1 = m - l + 1;
            int n2 = r - m;
            int L[] = new int [n1];
            int R[] = new int [n2];
            for (int i=0; i<n1; i++) {
                L[i] = list[l + i];	acc++;
            }
            for (int j=0; j<n2; j++) {
                R[j] = list[m + 1+ j];	acc++;
            }
            int i = 0, j = 0;
            int k = l;
            while (i < n1 && j < n2 && sorting) {
                check = k;
                if (L[i] <= R[j]) {
                    list[k] = L[i];	acc++;
                    i++;
                } else {
                    list[k] = R[j];	acc++;
                    j++;
                }
                compare++;
                Update();
                delay();
                k++;
            }
            while (i < n1 && sorting) {
                list[k] = L[i];	acc++;
                i++;
                k++;
                Update();
                delay();
            }
            while (j < n2 && sorting) {
                list[k] = R[j];	acc++;
                j++;
                k++;
                Update();
                delay();
            }
        }
        public void mergeSort(int l, int r) {
            if (l < r) {
                int m = (l+r)/2;
                current = r;
                mergeSort(l, m);
                mergeSort(m+1, r);

                merge(l, m, r);
            }
        }
        public void jesusSort(int n) {
            int mi = 0;
            int size = len-mi+1;
            int temp = spd;
            spd = 0;
            int[] holes = new int[size];
            for(int x:list)	{
                holes[x-mi] += 1;
            }
            int i = 0;
            for(int count = 0; count < size; count++) {
                while(holes[count] > 0 && sorting) {
                    holes[count]--;
                    check = i;
                    list[i] = count+mi;	acc++;
                    i++;
                    Update();
                    delay();
                }
            }
            spd = temp;
        }
        public void swap(int i1, int i2) {
            int temp = list[i1];	acc++;
            list[i1] = list[i2];	acc+=2;
            list[i2] = temp;	acc++;
        }
    }
}
