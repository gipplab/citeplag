#include "stdio.h"
#include "stdlib.h"
#include "string.h"
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>


//emulated 128 bit type
#if 1
#define halfword_t __uint64_t
typedef struct
{
  halfword_t lo,hi;
}word_t;
#define eqword(x,y) (x.lo==y.lo && x.hi==y.hi)
#define ltword(x,y) (x.hi<y.hi || x.hi==y.hi && x.lo<y.lo)
#define readat(x,y,z) {x.lo=halfread(y,z);x.hi=halfread(y,z+sizeof(halfword_t));}
#else
//typedef __uint128_t word_t;
 typedef __uint64_t word_t;
//typedef __uint32_t word_t;
#define halfword_t word_t
#define readat(x,y,z) x=halfread(y,z)
#define eqword(x,y) (x==y)
#define ltword(x,y) (x<y)
#endif
typedef struct 
{
	int shared_ngrams;
	int shared_distinct_ngrams;//old kv
	
} tkv;
typedef int t_int;
int verbose=0;
#define USE_UNALIGNED_READS
//CG rsort
#define fr(x,y)for(int x=0;x<y;x++)



//#pragma css task input (x[l], l, DEPTH,taskid) output (retval[l-DEPTH+1])
void simpler_rsort4ngrams(unsigned char *x, int l, int DEPTH,int taskid,int *retval)
{
	if(verbose)printf("starting task %d\n",taskid);
	int NN=l-DEPTH+1;
	if(NN>0)
	{
		unsigned char *pin=x+NN;
		unsigned char *pout=x;
		t_int *ix=retval;
		t_int *ox=(t_int*)malloc(NN*sizeof(t_int));
		const int RANGE=256;
		t_int counters[RANGE];
		t_int startpos[RANGE];

		fr(i,NN)ix[i]=i;
		//radix sort, the input is x, the output rank is ix
		//counters
		fr(k,RANGE)counters[k]=0;
		fr(i,NN)counters[*(x+i)]++;//counters[byte(v[i],ofs+LAST_BYTE-(DEPTH-1))]++;//counters[v[ofs+i]]++;
		fr(j,DEPTH)
		{
	//		fr(i,256)printf("%d ",counters[i]);	printf("\n");
			int ofs=j;//low endian
			t_int sp=0;
			fr(k,RANGE)
			{
				startpos[k]=sp;
				sp+=counters[k];
			}
			fr(i,NN)
			{
				unsigned char c=x[ofs+ix[i]];//byte(v[ix[i]],ofs+LAST_BYTE-(DEPTH-1));//v[ofs+ix[i]];
				ox[startpos[c]++]=ix[i];
			}
			memcpy(ix,ox,NN*sizeof(ix[0]));//fr(i,NN)ix[i]=ox[i];
			//update counters
			if(j<DEPTH-1)
			{
				counters[*pout++]--;
				counters[*pin++]++;
			}
		}

		//free(ix);
		free(ox);
	}
	if(verbose)printf("ending task %d\n",taskid);
}

#define MAXBUFSIZ 8000123

#ifdef USE_UNALIGNED_READS
#define halfread(buf,poz) (*(halfword_t *)(buf+poz))
#else
halfword_t halfread(unsigned char *buf,int poz)
{
	int depth=sizeof(halfword_t);
	halfword_t rez=0;
	fr(i,depth){rez<<=8;rez|=buf[poz+depth-1-i];}
	return rez;
}
#endif

typedef struct st_processed
{
	unsigned char *buf;
	int *ix;
	int len;
} processed;

processed *indexed_files;

#define MFNL 1024
//#pragma css task input(name[1024]) output(adrl1[1],adrfile1[1])
void readfile(char *name,int *adrl1,unsigned char **adrfile1)
{
	if(verbose)printf("reading %s ... ",name);

	  unsigned char * file1=(unsigned char * )malloc(MAXBUFSIZ);
	  *adrfile1=file1;
	  if(!file1)

	  {

		printf("unable to allocate!\n");

		exit(1);

	  }

	  FILE *f1=fopen(name,"rb");

	  if(!f1)

	  {

		printf("unable to open the file %s!\n",name);

		exit(1);

	  }

	  int l1=fread(file1,1,MAXBUFSIZ,f1);
	  *adrl1=l1;

	  fclose(f1);

	  if(verbose)printf("done\n");
}

//#pragma css task input(sz, depth) output(out[1])
void allocateindex(int *sz, int depth,t_int **out)
{
	*out=(t_int*)malloc((*sz-depth+1)*sizeof(t_int));
}



#pragma css task input(name[1024], depth, i) output (idx_f)
void ppsinglefile (char *name, processed *idx_f, int depth, int i) {
	  readfile(name,&(idx_f->len),&(idx_f->buf));
//#pragma css barrier
	  allocateindex(&(idx_f->len),depth,&(idx_f->ix));
//#pragma css barrier
	  simpler_rsort4ngrams(idx_f->buf,idx_f->len,depth,i,idx_f->ix);
}




int preprocess_files(char * names[],int nrfiles)
{
	int depth=sizeof(word_t);

	indexed_files=(processed *)malloc(nrfiles*sizeof(processed));

	fr(i,nrfiles)
	{
		ppsinglefile(names[i], &indexed_files[i], depth, i);
/*		  readfile(names[i],&(indexed_files[i].len),&(indexed_files[i].buf));
#pragma css barrier
		  allocateindex(&(indexed_files[i].len),depth,&(indexed_files[i].ix));
#pragma css barrier
  		  simpler_rsort4ngrams(indexed_files[i].buf,indexed_files[i].len,depth,i,indexed_files[i].ix);
*/	}
//#pragma css barrier
	return nrfiles;
}

//#pragma css task input(taskid,file1[l1],l1,ix1[l1-sizeof(word_t)+1],file2[l2],l2,ix2[l2-sizeof(word_t)+1]) output(retval[1])
void kernelvalue(int taskid,unsigned char * file1,int l1,int *ix1,unsigned char * file2,int l2,int *ix2,tkv *retval)
{
	if(verbose)printf("starting KV task %d\n",taskid);

	int depth=sizeof(word_t);

    //comparison based on merge
	int i1=0;
	int i2=0;
	l1-=(depth-1);
	l2-=(depth-1);
	int c1=0;
	int c2=0;
	int cc=0;
	int cc_all=0;
	word_t ls;
	int firstrun=1;
	if(ix1 && ix2)
	{
		word_t s1;readat(s1,file1,ix1[i1]);
		word_t s2;readat(s2,file2,ix2[i2]);

		while(i1<l1 && i2<l2)
		{
	//		fprintf(stderr,"i1=%d i2=%d s1=%0llx s2=%0llx\n",i1,i2,s1,s2);
			if(eqword(s1,s2))
			{
				cc_all++;
				if(firstrun || !eqword(s1,ls)){cc++;ls=s1;firstrun=0;}
				//printf("%d %d %d %lx\n",ix1[i1],ix2[i2],cc,s1);
				i1++;
				if(i1<l1)readat(s1,file1,ix1[i1]);
				i2++;
				if(i2<l2)readat(s2,file2,ix2[i2]);
			}
			else if(ltword(s1,s2))
			{
				i1++;
				if(i1<l1)readat(s1,file1,ix1[i1]);
			}
			else if(ltword(s2,s1))
			{
				i2++;
				if(i2<l2)readat(s2,file2,ix2[i2]);
			}
		}
	}
	retval->shared_ngrams=cc_all;
	retval->shared_distinct_ngrams=cc;
	if(verbose)printf("ending KV task %d\n",taskid);
}


#pragma css task input(i,j, idx_fi, idx_fj) output(mat[1])
void callkernelval(int i, int j, processed *idx_fi, processed *idx_fj, tkv *mat) {
	kernelvalue(100000*(i+1)+(j+1),idx_fi->buf,idx_fi->len,idx_fi->ix,idx_fj->buf,idx_fj->len,idx_fj->ix,mat);
}



//compute the kernel matrix
tkv ** computekernelmatrix(int nrfiles)
{
	tkv **mat=(tkv**)malloc(sizeof(tkv*)*nrfiles);
	fr(i,nrfiles)mat[i]=(tkv*)malloc(nrfiles*sizeof(tkv));

	fr(i,nrfiles)
	fr(j,nrfiles)
	if(i<=j) callkernelval(i,j,&indexed_files[i], &indexed_files[j], &mat[i][j]);
//		kernelvalue(100000*(i+1)+(j+1),indexed_files[i].buf,indexed_files[i].len,indexed_files[i].ix,indexed_files[j].buf,indexed_files[j].len,indexed_files[j].ix,&(mat[i][j]));
#pragma css barrier
	fr(i,nrfiles)
	fr(j,nrfiles)
	if(i<j)mat[j][i]=mat[i][j];
	return mat;
}

int main(int argc, char ** argv)
{
#pragma css start
	//get the names from stdin
	#define MXNAMES 50000
	char *names[MXNAMES];
	char buf[MFNL];
	int nrfiles=0;
	while(fgets(buf,MFNL,stdin))
	{
		if(buf[strlen(buf)-1]=='\n')buf[strlen(buf)-1]=0;
		char *bc=(char *)malloc(strlen(buf)+1);
		strcpy(bc,buf);
		names[nrfiles++]=bc;
		if(verbose)printf("%s\n",names[nrfiles-1]);
	}

	preprocess_files(names,nrfiles);
	tkv **mat=computekernelmatrix(nrfiles);

	if(1)
	fr(i,nrfiles)
	{
		fr(j,nrfiles)printf("%d %d  ",mat[i][j].shared_ngrams,mat[i][j].shared_distinct_ngrams);
		printf("\n");
	}
#pragma css finish
  	return 0;
}
