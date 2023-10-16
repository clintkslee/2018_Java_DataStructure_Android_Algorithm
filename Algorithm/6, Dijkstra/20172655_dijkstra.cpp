//Dijkstra's Algorithm
//20172655 LEE KANG SAN
//UTF-8

#include <iostream>
#define INFINITE 999

typedef int index;
typedef int number;
typedef struct
{
    int start;
    int end;
    int weight;
}edge;
typedef edge* set_of_edges;

void buildGraph(int n, number**& W);
void printGraph(int n, number** W);
void buildEdgeArr(int n, set_of_edges& F);
void dijkstra(int n, number** W, set_of_edges& F);

int main()
{
    int n; //number of vertices 
    int num_of_edge = 0;
    number** W;
    set_of_edges F; 

    int check;
    int cnt=1;
    int input1, input2, input3;

    printf("Dijkstra's Algorithm\n");
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("정점 개수(n)를 입력하세요 : ");
    scanf("%d", &n);
    buildGraph(n, W);
    buildEdgeArr(n, F);
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("간선들을 입력하세요. end 입력 시 종료합니다\n");
    printf("v1 v2 weight 꼴로 입력합니다.\n");
    printf("예) (v4, v6) weight=3 인 경우 : 4 6 3\n");
    while(cnt)
    {
        printf("입력(%d) : ", cnt++);
        check=scanf("%d %d %d", &input1, &input2, &input3);
        if(check!=3) 
        {
            printf("<<간선 입력 완료>>\n");
            break; //end 입력 시 간선 입력 종료
        }
        W[input1][input2]=input3; //(input1)->(input2) 간선의 가중치 input3
    }
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("<<가중치포함 방향그래프(W)의 인접행렬>>\n");
    printGraph(n, W);
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    dijkstra(n, W, F);
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("<<최단 경로 상 이음선의 집합 F>>\n");
    for(int i=0; i<=n-2; i++)
    {
        if(F[i].weight==INFINITE)
            printf("(%d) -> (%d), weight = INFINITE\n", F[i].start, F[i].end);
        else    
            printf("(%d) -> (%d), weight = %d\n", F[i].start, F[i].end, F[i].weight);
    }
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("<<끝>>\n");
    ///////////////////////////
    for(int i=0; i<=n; i++)
        free(W[i]);
    free(W);
    return 0;
}

void buildGraph(int n, number**& W)
{
    index i, j;
    W=(number**)malloc(sizeof(int*)*(n+1));
    for(i=0; i<=n; i++)
        W[i]=(int*)malloc(sizeof(int)*(n+1));
    for(i=1; i<=n; i++)
        for(j=1; j<=n; j++)
            W[i][j]=INFINITE;
    for(i=1; i<=n; i++)
        W[i][i]=0;
}
void printGraph(int n, number** W)
{
    for(int i=1; i<=n; i++)
    {
        for(int j=1; j<=n; j++)
        {
            if(W[i][j]==INFINITE)
                printf("x  ");
            else
                printf("%-3d", W[i][j]);
        }
        printf("\n");
    }
}
void buildEdgeArr(int n, set_of_edges& F)
{
    F=(set_of_edges)malloc(sizeof(edge)*(n-1));
}

void dijkstra(int n, number** W, set_of_edges& F)
{
    index i, vnear;
    edge e;
    int min;
    int indexF=0;
    index* touch=(index*)malloc(sizeof(index)*(n+1));
    number* length=(number*)malloc(sizeof(number)*(n+1));
    
    for(i=2; i<=n; i++)
    {
        touch[i]=1;
        length[i]=W[1][i];
    }

    for(int cnt=1; cnt<=n-1; cnt++) //repeat n-1 times
    {
        min=INFINITE;
        for(i=2; i<=n; i++)
        {
            if(0<=length[i]&&length[i]<=min)
            {
                min=length[i];
                vnear=i;
            }
        }
        e.start=touch[vnear];
        e.end=vnear;
        e.weight=W[touch[vnear]][vnear];

        F[indexF].start=e.start;
        F[indexF].end=e.end;
        F[indexF].weight=e.weight;
        indexF++;

        for(i=2; i<=n; i++)
        {
            if(length[vnear]+W[vnear][i]<length[i])
            {
                length[i]=length[vnear]+W[vnear][i];
                touch[i]=vnear;
            }
        }

        length[vnear]=-1;
    }
    printf("<<touch 배열>>\n");
    for(i=2; i<=n; i++)
        printf("touch[%d] : %d\n", i, touch[i]);
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("<<length 배열>>\n");
    for(i=2; i<=n; i++)
        printf("length[%d] : %d\n", i, length[i]);
    free(touch);
    free(length);
}


















