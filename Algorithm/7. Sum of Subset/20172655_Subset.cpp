//The Sum of Subsets Problem
//20172655 LEE KANG SAN
//UTF-8

#include <iostream>
#include <algorithm>
#define YES 1
#define NO 0

using namespace std;

typedef int index;

int n;
int W;
int* w;
int* include;
int total;

void sum_of_subsets(index i, int weight, int total);
bool promising(index i, int weight);

int main()
{
    total=0;
    printf("< The Sum of Subsets Problem >\n");
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("목표 합(W)를 입력하세요 : ");
    scanf("%d", &W);
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("weight의 개수(n)를 입력하세요 : ");
    scanf("%d", &n);
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    w=(int*)malloc(sizeof(int)*(n+1));
    include=(int*)malloc(sizeof(int)*(n+1));
    printf("%d개의 weight를 입력하세요.\n", n);
    for(int i=1; i<=n; i++)
    {
        printf("입력(%d) : ", i);
        scanf("%d", &w[i]);
        total+=w[i];
    }
    sort(w, w+(n+1));
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("<각 weight들의 값>\n");
    for(int i=1; i<=n; i++)
        printf("w%d = %d\n", i, w[i]);
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("<합이 W=%d 가 되는 모든 정수의 조합>\n", W);
    sum_of_subsets(0, 0, total);
    printf("*** *** *** *** *** *** *** *** *** ***\n");
    printf("<끝>\n");
    //////////
    free(w);
    free(include);
}
void sum_of_subsets(index i, int weight, int total)
{
    if(promising(i, weight))
    {
        if(weight==W)
        {
            for(int j=1; j<=i; j++)
            {
                if(include[j])
                    printf("[w%d] ", j);
            }
            printf("\n");
        }
        else
        {
            include[i+1]=YES;
            sum_of_subsets(i+1, weight+w[i+1], total-w[i+1]);
            include[i+1]=NO;
            sum_of_subsets(i+1, weight, total-w[i+1]);
        }
    }
}
bool promising(index i, int weight)
{
    return (weight+total>=W)&&(weight==W||weight+w[i+1]<=W);
}
    
    

