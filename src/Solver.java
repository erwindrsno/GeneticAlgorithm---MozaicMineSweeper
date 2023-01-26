
class Solver{
    int[][] board;
    String[][] check;
    int[][] answer;
    int[] chromosomeSolution;
    int length;

    public Solver(int[][] board, String[][] check, int[][] answer, int length){
        this.board = board;
        this.check = check;
        this.answer = answer;
        this.length = length;
        this.chromosomeSolution = new int[length];
    }

    public void begin(){
        do{
            solve();
        } while(checkAll() == false);

        assign();
        System.out.println("Solution: ");
        printBoard();
    }

    public int[] convertToArray(){
        int k = 0;

        for (int i = 1; i < answer.length-1; i++) {
            for (int j = 1; j < answer.length-1; j++) {
                chromosomeSolution[k] = answer[i][j];
                k++;
            }
        }
        return chromosomeSolution;
    }

    //untuk memeriksa jika terdapat kotak yang belum diperiksa, maka akan return false
    public boolean checkAll(){
        boolean valid = true;
        for (int i = 1; i < board.length-1; i++) {
            for (int j = 1; j < board.length-1; j++) {
                if(check[i][j].equals("bisajadi")){
                    valid = false;
                    break;
                }
            }
        }
        return valid;
    }

    //method untuk menyelesaikan puzzle
    public void solve(){
        for (int i = 1; i < board.length-1; i++) {
            for (int j = 1; j < board.length-1; j++) {
                //kalo jumlah kotak di sekitar angkaX == angkaX, kotak diisi string "ya"
                //kalo angkaX == 0, kotak diisi string "tidak"
                if(board[i][j]==0){
                    if(check[i][j].equals("bisajadi")){
                        check[i][j]="tidak";
                    }
                    if(check[i+1][j].equals("bisajadi")){
                        check[i+1][j]="tidak";
                    }
                    if(check[i-1][j].equals("bisajadi")){
                        check[i-1][j]="tidak";
                    }
                    if(check[i][j+1].equals("bisajadi")){
                        check[i][j+1]="tidak";
                    }
                    if(check[i][j-1].equals("bisajadi")){
                        check[i][j-1]="tidak";
                    }
                    if(check[i+1][j+1].equals("bisajadi")){
                        check[i+1][j+1]="tidak";
                    }
                    if(check[i+1][j-1].equals("bisajadi")){
                        check[i+1][j-1]="tidak";
                    }
                    if(check[i-1][j+1].equals("bisajadi")){
                        check[i-1][j+1]="tidak";
                    }
                    if(check[i-1][j-1].equals("bisajadi")){
                        check[i-1][j-1]="tidak";
                    }
                }else if(board[i][j]==4 && ((i==1 && (j==1||j==board.length-2))||(i==board.length-2 && (j==1||j==board.length-2)))){
                    check[i][j]="iya";
                    check[i+1][j]="iya";
                    check[i-1][j]="iya";
                    check[i][j+1]="iya";
                    check[i][j-1]="iya";
                    check[i+1][j+1]="iya";
                    check[i+1][j-1]="iya";
                    check[i-1][j+1]="iya";
                    check[i-1][j-1]="iya";
                }
                else if(board[i][j]==6 && (i==1&&j>1&&j<board.length-2||i==board.length-2&&j>1&&j<board.length-2||j==1&&i>1&&i<board.length-2||j==board.length-2&&i>1&&i<board.length-2)){
                    check[i][j]="iya";
                    check[i+1][j]="iya";
                    check[i-1][j]="iya";
                    check[i][j+1]="iya";
                    check[i][j-1]="iya";
                    check[i+1][j+1]="iya";
                    check[i+1][j-1]="iya";
                    check[i-1][j+1]="iya";
                    check[i-1][j-1]="iya";
                }
                //kalo jumlah kotak "ya" di sekitar angka X == angkaX, kotak lainnya diisi "tidak"
                else if(board[i][j]>=0){
                    int hitungIya = 0;
                    int hitungTidak = 0;
                    int hitungKotak = 0;
                    if(i>1&&i<board.length-2&&j>1&&j<board.length-2){
                        hitungKotak=9;
                    }else if((i==1&&(j==1||j==board.length-2))||(i==board.length-2 && (j==1||j==board.length-2))){
                        hitungKotak=4;
                    }else{
                        hitungKotak=6;
                    }
                    if(check[i][j].equals("iya")){
                        hitungIya++;
                    }else if(check[i][j].equals("tidak")){
                        hitungTidak++;
                    }
                    if(check[i+1][j].equals("iya")&&i+1!=board.length-1){
                        hitungIya++;
                    }else if(check[i+1][j].equals("tidak")&&i+1!=board.length-1){
                        hitungTidak++;
                    }
                    if(check[i-1][j].equals("iya")&&i-1!=0){
                        hitungIya++;
                    }else if(check[i-1][j].equals("tidak")&&i-1!=0){
                        hitungTidak++;
                    }
                    if(check[i][j+1].equals("iya")&&j+1!=board.length-1){
                        hitungIya++;
                    }else if(check[i][j+1].equals("tidak")&&j+1!=board.length-1){
                        hitungTidak++;
                    }
                    if(check[i][j-1].equals("iya")&&j-1!=0){
                        hitungIya++;
                    }else if(check[i][j-1].equals("tidak")&&j-1!=0){
                        hitungTidak++;
                    }
                    if(check[i+1][j+1].equals("iya")&&i+1!=board.length-1&&j+1!=board.length-1){
                        hitungIya++;
                    }else if(check[i+1][j+1].equals("tidak")&&i+1!=board.length-1&&j+1!=board.length-1){
                        hitungTidak++;
                    }
                    if(check[i+1][j-1].equals("iya")&&i+1!=board.length-1&&j-1!=0){
                        hitungIya++;
                    }else if(check[i+1][j-1].equals("tidak")&&i+1!=board.length-1&&j-1!=0){
                        hitungTidak++;
                    }
                    if(check[i-1][j+1].equals("iya")&&i-1!=0&&j+1!=board.length-1){
                        hitungIya++;
                    }else if(check[i-1][j+1].equals("tidak")&&i-1!=0&&j+1!=board.length-1){
                        hitungTidak++;
                    }
                    if(check[i-1][j-1].equals("iya")&&i-1!=0&&j-1!=0){
                        hitungIya++;
                    }else if(check[i-1][j-1].equals("tidak")&&i-1!=0&&j-1!=0){
                        hitungTidak++;
                    }
//                    System.out.println(hitungKotak);
                    if(board[i][j]==hitungIya){
                        if(check[i][j].equals("bisajadi")){
                            check[i][j]="tidak";
                        }
                        if(check[i+1][j].equals("bisajadi")&&i+1!=board.length-1){
                            check[i+1][j]="tidak";
                        }
                        if(check[i-1][j].equals("bisajadi")&&i-1!=0){
                            check[i-1][j]="tidak";
                        }
                        if(check[i][j+1].equals("bisajadi")&&j+1!=board.length-1){
                            check[i][j+1]="tidak";
                        }
                        if(check[i][j-1].equals("bisajadi")&&j-1!=0){
                            check[i][j-1]="tidak";
                        }
                        if(check[i+1][j+1].equals("bisajadi")&&i+1!=board.length-1&&j+1!=board.length-1){
                            check[i+1][j+1]="tidak";
                        }
                        if(check[i+1][j-1].equals("bisajadi")&&i+1!=board.length-1&&j-1!=0){
                            check[i+1][j-1]="tidak";
                        }
                        if(check[i-1][j+1].equals("bisajadi")&&i-1!=0&&j+1!=board.length-1){
                            check[i-1][j+1]="tidak";
                        }
                        if(check[i-1][j-1].equals("bisajadi")&&i-1!=0&&j-1!=0){
                            check[i-1][j-1]="tidak";
                        }
                    }
                    //kalo jumlah kotak "tidak" di sekitar angka X == (jumlah kotak di sekitar - angkaX), kotak lainnya diisi "ya"
                    if(hitungKotak-board[i][j]==hitungTidak){
                        if(check[i][j].equals("bisajadi")){
                            check[i][j]="iya";
                        }
                        if(check[i+1][j].equals("bisajadi")&&i+1!=board.length-1){
                            check[i+1][j]="iya";
                        }
                        if(check[i-1][j].equals("bisajadi")&&i-1!=0){
                            check[i-1][j]="iya";
                        }
                        if(check[i][j+1].equals("bisajadi")&&j+1!=board.length-1){
                            check[i][j+1]="iya";
                        }
                        if(check[i][j-1].equals("bisajadi")&&j-1!=0){
                            check[i][j-1]="iya";
                        }
                        if(check[i+1][j+1].equals("bisajadi")&&i+1!=board.length-1&&j+1!=board.length-1){
                            check[i+1][j+1]="iya";
                        }
                        if(check[i+1][j-1].equals("bisajadi")&&i+1!=board.length-1&&j-1!=0){
                            check[i+1][j-1]="iya";
                        }
                        if(check[i-1][j+1].equals("bisajadi")&&i-1!=0&&j+1!=board.length-1){
                            check[i-1][j+1]="iya";
                        }
                        if(check[i-1][j-1].equals("bisajadi")&&i-1!=0&&j-1!=0){
                            check[i-1][j-1]="iya";
                        }
                    }
                }
            }
        }
    }

    //assign board solution berdasarkan minesweeper check
    public void assign(){
        for (int i = 1; i < check.length-1; i++) {
            for (int j = 1; j < check.length-1; j++) {
                if(check[i][j].equals("iya")){
                    answer[i][j]=1;
                }else if(check[i][j].equals("tidak")){
                    answer[i][j]=0;
                }
            }
        }
    }

    public void printBoard(){
        for (int i = 1; i < board.length-1; i++) {
            for (int j = 1; j < board.length-1; j++) {
                System.out.print(answer[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println("======================================================");
    }
}