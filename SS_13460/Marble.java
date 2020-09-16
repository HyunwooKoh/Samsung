package SS_13460;

import java.io.*;
import java.util.*;


public class Marble {

    class info {
        public int rx, ry, bx, by;
        info(int x_r, int y_r, int x_b, int y_b) {
            this.rx = x_r; this.ry = y_r;
            this.bx = x_b; this.by = y_b;
        }
    }

    public char[][] plate;
    public int[][] visit_R;
    public int[][] visit_B;
    public int x_size, y_size;
    public int r_x, r_y, b_x, b_y;
    public int o_x, o_y;
    public int m_x[] = {0, 0, -1, 1};
    public int m_y[] = {-1, 1, 0, 0};
    
    public void init() {
        
        Scanner sc = new Scanner(System.in);

        System.out.print("insert x, y : ");
        x_size = sc.nextInt();
        y_size = sc.nextInt();
        sc.nextLine();
        plate = new char[x_size][y_size];
        visit_R = new int[x_size][y_size];
        visit_B = new int[x_size][y_size];

        for(int i = 0 ; i < y_size ; i++) {
            System.out.print("Insert" + (i+1) + "lines : ");
            String temp_string = sc.nextLine();
            char temp[] = temp_string.toCharArray();
            if(temp.length != x_size) {
                System.out.println("error 1");
                return;
            }
            else {
                for (int j = 0 ; j < x_size ; j++ ) {
                    switch(temp[j]) {
                        case '#' : 
                            plate[j][i] = temp[j];
                            break;
                        case '.' :
                            plate[j][i] = temp[j];
                            break;
                        case 'R' :
                            r_x = j; r_y = i;
                            plate[j][i] = temp[j];
                            break;
                        case 'B' :
                            b_x = j; b_y = i;
                            plate[j][i] = temp[j];
                            break;
                        case 'O' :
                            o_x = j; o_y = i;
                            plate[j][i] = temp[j];
                            break;
                        default : 
                            break;
                    }
                }
            }
        }
        
        sc.close();
    }

    public void init_visit() {
        for(int i = 0 ; i < y_size ; i ++) {
            for(int j = 0 ; j < x_size ; j++ ) {
                visit_B[j][i] = visit_R[j][i] = -1;
            }
        }
        visit_R[r_x][r_y] = visit_B[b_x][b_y] = 0;
    }

    public int FindPrime() {
        
        Queue<info> q = new LinkedList<info>();

        q.add(new info(r_x,r_y,b_x,b_y));
        init_visit();
        while(!q.isEmpty()) {
            info temp = q.poll();
            for(int i = 0; i < 4 ; i++) {
                int rx = temp.rx , ry = temp.ry;
                int bx = temp.bx , by = temp.by;
                
                while(true) {
                    rx = rx + m_x[i]; ry = ry + m_y[i];
                    bx = bx + m_x[i]; by = by + m_y[i];
                    
                    if(rx < 0 || ry < 0 || bx < 0 || by < 0) break;
                    if(rx >= x_size || ry >= y_size || bx >= x_size || by >= y_size) break;
                    if(visit_R[rx][ry] != -1) break;

                    // 두 구슬 모두 다음 칸으로 이동할 수 있는 경우
                    if(plate[rx][ry] == '.' && plate[bx][by] == '.') {
                        plate[rx][ry] = 'R'; plate[bx][by] = 'B';
                        plate[rx - m_x[i]][ry - m_y[i]] = 'R'; plate[bx - m_x[i]][by - m_y[i]] = 'B';
                        visit_R[rx][ry] = visit_R[temp.rx][temp.ry] + 1; 
                    }
                    // 파란색 구슬이 O으로 들어간 경우
                    else if(plate[bx][by] == 'O')
                        break;
                    // 붉은색 구슬이 0으로 들어간 경우
                    else if(plate[rx][ry] == 'O') {
                        return (visit_R[temp.rx][temp.ry] + 1);
                    }
                    // 두 구술중 하나 이상의 구술이 벽에 도달해 멈췄을 경우
                    else if(plate[rx][ry] == '#' || plate[bx][by] == '#') {
                        // 붉은색 구슬이 벽에 도달했을 경우
                        if(plate[rx][ry] == '#') {
                            while(true) {
                                bx = bx + m_x[i]; by = by + m_y[i];
                                if(plate[bx][by] == '#') {
                                    System.out.println("bx : " + bx + " by : " + by);
                                    q.add(new info(rx - m_x[i], ry - m_y[i], bx - m_x[i], by - m_y[i]));
                                    break;
                                }
                                else if(plate[bx][by] == 'O') break;
                                else 
                                    plate[bx - m_x[i]][by - m_y[i]] = 'B';
                            }
                        }
                        //파란색 구슬이 벽에 도달했을 경우
                        else {
                            while(true) {
                                rx = rx + m_x[i]; ry = ry + m_y[i];
                                if(plate[rx][ry] == '#') {
                                    q.add(new info(rx - m_x[i], ry - m_y[i], bx - m_x[i], by - m_y[i]));
                                    break;
                                }
                                else if(plate[bx][by] == 'O')
                                    return (visit_R[temp.rx][temp.ry] + 1);
                                else {
                                    plate[rx - m_x[i]][ry - m_y[i]] = 'R';
                                    visit_R[rx][ry] = visit_R[temp.rx][temp.ry] + 1;
                                }
                            }
                        }
                    }
                    // 두 구술이 겹쳐길 경우
                    else if(plate[rx][ry] == 'B' || plate[bx][by] == 'R') {
                        // 1. 붉은색 구슬의 다음 위치에 푸른색 구슬이 있을 경우
                        if(plate[rx][ry] == 'B') {
                            if(plate[bx][by] == 'O') break;
                            else if(plate[bx][by] == '#') {
                                q.add(new info(rx - m_x[i], ry - m_y[i], bx - m_x[i], by - m_y[i]));
                            }
                        }
                        // 2. 푸른색 구슬의 다음 위치에 붉은색 구슬이 있을 경우
                        else {
                            if(plate[rx][ry] == 'B') {
                                if(plate[bx][by] == 'O') break;
                                else if(plate[bx][by] == '#') {
                                    q.add(new info(rx - m_x[i], ry - m_y[i], bx - m_x[i], by - m_y[i]));
                                }
                            }
                        }
                    }
                }
            }
        }
        //구슬을 뺄 방법을 찾지 못한 경우
        return -1;
    }

    public static void main(String args[])  throws IOException {
        Marble mMarble = new Marble();
        mMarble.init();
        System.out.println("result : " + mMarble.FindPrime());
        return;
    }
}

