//Floyd's Algorithm for Shortest Paths
//20172655 이강산
//UTF-8

#include<iostream>
#include<cstdio>
#include<climits>
#define MAX 12345678 //두 vertax간 edge 없음을 나타낼 MAX 값
using namespace std;
void buildGraph(const int v, int**& G);
void inputGraph(const int v, int**& G);
void printGraph(const int v, int** G, char c);
void copyGraph(const int v, int**& G1, int**& G2);
void floyd2(const int v, int**& W, int**& D, int**& P);
void path(int q, int r, int**& P);
void printPath(int q, int r, int**& P);

int main()
{//그래프 입력 시 모든 두 정점 간의 경로 출력
    int **W, **D, **P; //그래프가 저장 될 W, v개의 loop 거쳐 최단경로 길이가 저장 될 D
    int v; //vertax 개수
    printf("**********  Floyd Algorithm  **********\n");
    printf("vertax의 번호는 1부터 시작\n");
    printf("vertax 개수 입력 : ");
    scanf("%d", &v);
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    buildGraph(v, W);
    inputGraph(v, W);
    floyd2(v, W, D, P);
    printGraph(v, W, 'W');
    printGraph(v, D, 'D');
    printGraph(v, P, 'P');
    printf("ALL PATHS\n");
    for(int i=1; i<=v; i++)// (1) -> (2) 부터 (v-1) -> (v) 까지 모든 경로 출력
    {
        for(int j=1; j<=v; j++)
        {
            if(i==j) continue;
            printf("(%d) to (%d) : ", i, j);
            if(D[i][j]!=MAX) printPath(i, j, P); // (i) -> (j) 경로에서 D 배열 값이 MAX == 경로 자체가 없음
            else printf("No Path!\n");
        }
    }
    return 0;
}

void buildGraph(const int v, int**& G)
{
    G=(int**)malloc(sizeof(int*)*(v+1));
    for(int i=0; i<=v; i++)
        G[i]=(int*)malloc(sizeof(int)*(v+1));
    for(int i=0; i<=v; i++)
        for(int j=0; j<=v; j++)
            G[i][j]=0;
}//index 계산 용이하기 위해 0번 index는 제외하였다.

void inputGraph(const int v, int**& G)
{
    int weight;
    printf("그래프 입력\n"); //인접행렬 입력
    printf("(i)==>(j) edge의 가중치 입력\n");
    printf("두 vertax 간 edge 없을 시 -1 입력\n"); //두 vetax간 edge 없을 시 -1 입력
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    for(int i=1; i<=v; i++)
    {// 그래프 입력 
        for(int j=1; j<=v; j++)
        {
            if(i==j)
            {
                printf("(%d)==>(%d) : 0 (same vertax)\n", i, j);
                G[i][j]=0; //시작정점 끝정점 동일 시 0
                continue;
            }
            printf("(%d)==>(%d) : ", i, j);
            scanf("%d", &weight);
            if(weight==-1) weight=MAX; //서로 다른 두 정점 간 edge 없을 시 MAX값 저장
            G[i][j]=weight;
        }
        printf("\n");
    }
    printf("INPUT FINISHED!\n");
    printf("*** *** *** *** *** *** *** *** *** ***\n");
}

void printGraph(const int v, int** G, char c)
{//그래프 출력
    printf("GRAPH : %c\n", c);
    for(int i=1; i<=v; i++)
    {
        for(int j=1; j<=v; j++)
        {
            if(G[i][j]==MAX) 
            {
                printf("MAX ");
                continue;
            }
            printf("%-4d", G[i][j]);
        }
        printf("\n");
    }
    printf("*** *** *** *** *** *** *** *** *** ***\n");
}

void copyGraph(const int v, int**& G1, int**& G2)
{//G1 그래프의 내용을 새로 만든 G2 그래프에 복사
    buildGraph(v, G2);
    for(int i=0; i<=v; i++)
        for(int j=1; j<=v; j++)
            G2[i][j]=G1[i][j];
}

void floyd2(const int v, int**& W, int**& D, int**& P)
{
    buildGraph(v, P); //P배열 생성
    for(int i=0; i<=v; i++)
        for(int j=1; j<=v; j++)
            P[i][j]=0;
    copyGraph(v, W, D); //D = W, W 배열 복사한 D 배열 생성
    for(int k=1; k<=v; k++)
    { 
        for(int i=1; i<=v; i++)
        {
            for(int j=1; j<=v; j++)
            {
                if(D[i][k]+D[k][j]<D[i][j])
                {
                    P[i][j]=k;
                    D[i][j]=D[i][k]+D[k][j];
                }
            }
        }
    }
}

void path(int q, int r, int**& P)
{
    if(P[q][r] != 0)
    {
        path(q, P[q][r], P);
        printf("%d -> ", P[q][r]);
        path(P[q][r], r, P);
    }
    else; 
    //P[q][r] 값이 0이면 q와 r사이 최단거리에 반드시 필요한 정점이 없음
}

void printPath(int q, int r, int**& P)
{//path() 함수 이용하여 q -> r 간 경로 출력
    printf("%d -> ", q);
    path(q, r, P);
    printf("%d\n", r);
}
