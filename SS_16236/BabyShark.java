package SS_16236;

import java.io.*;
import java.util.*;

public class BabyShark {
    
    public int size;
    public int sec = 0, S_size = 2, S_count = 0;
    public int S_x, S_y;
    public int[][] plate;
    public int[][] visit;
    
    // 이동가능한 방향은 총 4가지.
    // 좌측 및 상향이동은 음수, 우측 및 하향 이동은 양수  
    public int[] d_x = {0, 0, 1, -1};
    public int[] d_y = {-1, 1, 0, 0};

    class square {
        int x, y;
        square(int x , int y) {
            this.x = x;
            this.y = y;
        }
    };

    public boolean FindPrime() {
        // 최적의 먹잇감을 찾아주는 함수
        int min_dist = size * size; //판 내에서 생길 수 있는 최대 거리로 초기화
        int min_x = size , min_y = size; // index가 큰 판 내부에 없는 칸으로 초기화, 
        Queue<square> p = new LinkedList<square>();
        
        init_start();
        visit[S_x][S_y] = 0;
        p.add(new square(S_x,S_y));
        
        while(!p.isEmpty()) {

            square temp = p.poll();

            for(int i = 0 ; i < 4 ; i++) {
                    
                int nx = temp.x + d_x[i];
                int ny = temp.y + d_y[i];

                // plate 밖으로 나가는 경우
                if(nx < 0 || nx >= size || ny < 0 || ny >= size) continue; 
                // 이미 지난 칸인지 확인 || 아기상어의 크기보다 큰 물고기가 있는 경우
                if(visit[nx][ny] != -1 || plate[nx][ny] > S_size) continue; 

                // 해당 위치의 거리 업로드
                visit[nx][ny] = visit[temp.x][temp.y] + 1;

                // 먹잇감이 있는지 확인
                if(plate[nx][ny] != 0 && plate[nx][ny] < S_size) {

                    // 현재 최단거리보다 짧은 경우
                    if(min_dist > visit[nx][ny]) {
                        min_x = nx; min_y = ny;
                        min_dist = visit[nx][ny];
                    }
                    // 현재 최단거리와 같은 경우
                    else if(min_dist == visit[nx][ny]) {
                       
                        // 조건 1 : 가장 위측에 있는 먹이를 우선
                        if(min_y > ny) {
                            min_x = nx; min_y = ny;
                        }
                        // 조건 2 : 높이가 같으면, 가장 좌측에 있는 먹이를 우선
                        else if(min_y == ny && min_x > nx){
                            min_x = nx; min_y = ny;
                        }

                    }
                }    
                p.add(new square(nx, ny));
            }
        }
        // 먹이를 찾은 경우
        if(min_dist != size * size) {

            sec += min_dist;
            S_count++;
            plate[min_x][min_y] = 0;
            S_x = min_x; S_y = min_y;

            if(S_count == S_size) {
                S_count = 0; S_size++;
            }
            return true;
        }
        // 더이상 먹을 수 있는 먹이가 없거나, 고립된 경우
        else 
            return false;
    }      

    // 먹이를 찾기 전에 거리를 저장하는 지도를 초기화
    public void init_start() {
        for(int i = 0 ; i < size ; i++)
            for(int j = 0 ; j < size ; j++)
                visit[j][i] = -1;
    }

    // 초기화
    public void init(int p_size, int[][] p, int sx, int sy){
        this.plate = p;
        this.visit = new int[p_size][p_size];
        this.size = p_size;
        this.S_x = sx; this.S_y = sy;
    }

    public static void main(String[] args) throws IOException {
        //입력받아서 넘겨주기
        Scanner sc = new Scanner(System.in);
        BabyShark shark = new BabyShark();
        int[][] pt;
        int msize,sx,sy;

        System.out.println("insert size : ");
        msize = sc.nextInt();
        
        sx = sy = msize;
        pt = new int[msize][msize];
        for(int i = 0 ; i < msize ; i++) { 
            
            System.out.print("insert " + (i+1) + " line : ");
            for(int j = 0 ; j < msize ; j++) {
                
                pt[j][i] = sc.nextInt();
                if(pt[j][i] == 9) {
                    pt[j][i] = 0;
                    sx = j ; sy = i;
                }
            }
        }

        sc.close();
        
        shark.init(msize,pt, sx, sy);
        while(true) {
            if(!shark.FindPrime()) break;
        }

        System.out.println("time : " + shark.sec);
        return;
    }
}
