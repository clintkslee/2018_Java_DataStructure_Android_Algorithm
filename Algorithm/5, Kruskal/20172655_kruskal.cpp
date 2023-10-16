//Kruskal's Algorithm
//20172655 LEE KANG SAN
//UTF-8

#include <iostream>
#define INFINITE 999

typedef struct
{
    int weight;
    int v1;
    int v2;
}set_of_edges;

void buildGraph(int n, int**& G);
void printGraph(int n, int**& G);
void buildEdgeArr(int n, int m, set_of_edges*& E, set_of_edges*& F);
int compare(const void* A, const void* B);
void kruskal(int n, int m, set_of_edges*& E, set_of_edges*& F);
void deleteAll(int n, int**&G, set_of_edges*& E, set_of_edges*& F);

////// for Data Structure II
typedef int index;
typedef index set_pointer;
struct nodetype
{
    index parent;
    int depth;
};
typedef nodetype* universe;
universe U;

void makeset(index i)
{
    U[i].parent=i;
    U[i].depth=0;
}

set_pointer find(index i)
{
    index j;
    j=i;
    while(U[j].parent!=j)
        j=U[j].parent;
    return j;
}

void merge(set_pointer p, set_pointer q)
{
    if(U[p].depth==U[q].depth)
    {
        U[p].depth=U[p].depth+1;
        U[q].parent=p;
    }
    else if(U[p].depth<U[q].depth)
        U[p].parent=q;
    else
        U[q].parent=p;
}

bool equal(set_pointer p, set_pointer q)
{
    if(p==q)
        return true;
    else
        return false;
}

void initial(int n)
{
    index i;
    for(i=1; i<=n; i++)
        makeset(i);
}
//////////////////////////////////

int main()
{
    int n;   //vertax 개수
    int m; //edge 개수
    int** G; //입력받을 비방향 그래프 G
    set_of_edges* E; //G 입력 시 edge 저장됨
    set_of_edges* F; //kruskal 시 사용될 edge 저장
    int cnt=1;
    
    printf("Kruskal's Algorithm\n");
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("그래프(G)의 정점 개수(n)를 입력하세요 : ");
    scanf("%d", &n);
    printf("그래프(G)의 간선 개수(m)를 입력하세요 : ");
    scanf("%d", &m);
    U=(universe)malloc(sizeof(struct nodetype)*(n+1));
    buildEdgeArr(n, m, E, F);
    buildGraph(n, G);
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("간선들의 가중치를 입력하세요. (비방향성 그래프 이므로 절반만 입력합니다)\n");
    printf("간선이 존재하지 않는 경우 -1을 입력하세요.\n");
    for(int i=1; i<=n; i++) //그래프 G입력, edge E에 저장됨
    {
        for(int j=i+1; j<=n; j++)
        {
            printf("(%d, %d) : ", i, j); 
            scanf("%d", &G[i][j]);
            if(G[i][j]==-1) G[i][j]=INFINITE; //간선이 없는 경우
            else
            {
                E[cnt].weight=G[i][j];
                E[cnt].v1=i;
                E[cnt].v2=j;
                cnt++;
            }
            G[j][i]=G[i][j];
        }
    }
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("<<가중치포함 비방향그래프 G (인접행렬)>>\n");
    printGraph(n, G);
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    kruskal(n, m, E, F);
    printf("<<G의 간선 집합 E (정렬됨, m=%d개)>>\n", m);
    for(int i=1; i<=m; i++)
        printf("edge = (%d, %d), weight = %d\n", E[i].v1, E[i].v2, E[i].weight);
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("<<최소비용 신장트리의 간선 집합 F (n-1=%d개)>>\n", n-1);
    for(int i=0; i<=n-2; i++)
        printf("edge = (%d, %d), weight = %d\n", F[i].v1, F[i].v2, F[i].weight);
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("<<끝>>\n");
    deleteAll(n, G, E, F);
}
void deleteAll(int n, int**&G, set_of_edges*& E, set_of_edges*& F)
{
    free(E);
    free(F);
    for(int i=0; i<=n; i++)
        free(G[i]);
    free(G);
    free(U);
}

void buildGraph(int n, int**& G)
{
    G=(int**)malloc(sizeof(int*)*(n+1));
    for(int i=0; i<=n; i++)
        G[i]=(int*)malloc(sizeof(int)*(n+1));
    for(int i=1; i<=n; i++)
        G[i][i]=0;
}
void printGraph(int n, int**& G)
{
    for(int i=1; i<=n; i++)
    {
        for(int j=1; j<=n; j++)
        {
            if(G[i][j]==INFINITE)
                printf("x  ");
            else
                printf("%-3d", G[i][j]);
        }
        printf("\n");
    }
}

void buildEdgeArr(int n, int m, set_of_edges*& E, set_of_edges*& F)
{
    E=(set_of_edges*)malloc(sizeof(set_of_edges)*(m+1));
    F=(set_of_edges*)malloc(sizeof(set_of_edges)*(n-1));
}

int compare(const void* A, const void* B) //edge 배열 qsort시 사용
{
    set_of_edges* ptrA = (set_of_edges*)A;
    set_of_edges* ptrB = (set_of_edges*)B;
    if(ptrA->weight<ptrB->weight) return -1;
    if(ptrA->weight>ptrB->weight) return 1;
    return 0;
}

void kruskal(int n, int m, set_of_edges*& E, set_of_edges*& F)
{
    index i, j;
    set_pointer p, q;
    int e = 0; //E배열에서 edge 선택 시 인덱스 제어
    int input = 0; //F배열에 edge 추가시 인덱스 제어

    qsort(E+1, m, sizeof(set_of_edges), compare); //E배열 속 m개의 간선 비내림차순 정렬
    initial(n);
    
    while(input<n-1)
    {
        e++;
        i = E[e].v1;
        j = E[e].v2;
        p = find(i);
        q = find(j);
        if(!equal(p,q))
        {
            merge(p, q);
            F[input].v1=E[e].v1;
            F[input].v2=E[e].v2;
            F[input].weight=E[e].weight;
            input++;
        }
    }
}



