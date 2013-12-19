library("nnet")
library("rpart")
library("rpart.plot")
library("class")
library("e1071")
library("xtable")
library("RWeka")
library("FSelector")


source("functions.R")


datos<-read.csv("dataset.csv",header=T,sep="\t")
dataset<-datos[,2:10]

dataset_neu<-cbind(dataset[,1:(length(dataset)-1)],
                       label=ifelse(dataset$label=="neutral","neutral","non_neutral"))


#Balanceamos usando sampling

neu<-dataset_neu[dataset_neu$label=="neutral",]

neu_data<-neu[sample(1:dim(neu)[1],size=76165,replace=F),]

dataset_neu_samp<-dataset_neu[c(as.numeric(row.names(neu_data)),
                                   which(dataset_neu$label=="non_neutral")),]

dataset_neu_samp<-dataset_neu_samp[order(as.numeric(rownames(dataset_neu_samp))),] 

neu_log<-Logistic(label~.,dataset_neu_samp)

neu_log2<-glm(label~.,family=binomial(logit), data=dataset_neu_samp)


neu_log3<-glm(label~.,family=binomial(logit), 
              data=dataset_neu_samp[,c("earthSub","earthNeu","earthPos","earthNeg","elhPos","elhNeg","label")])

neu_log4<-glm(label~.,family=binomial(logit), 
              data=dataset_neu_samp[,c("earthSub","earthNeu","elhPos","elhNeg","label")])

neu_tree<-rpart(label~.,dataset_neu_samp)

log_neu<-mycv_part(10,dataset_neu_samp,"label",Logistic)
perf_log_neu<-conf_int_perf(log_neu)



log_neu_feat<-mycv_part(10,dataset_neu_samp[,3:9],"label",Logistic)
perf_log_neu_feat<-conf_int_perf(log_neu_feat)

log_neu_feat2<-mycv_part(10,
                         dataset_neu_samp[,c("earthSub","earthNeu","elhPos","elhNeg","label")],
                         "label",Logistic)
perf_log_neu_feat2<-conf_int_perf(log_neu_feat2)


# Datasets de polaridad
dataset_pol<-subset(dataset,label!="neutral")
dataset_pol$label<-as.factor(as.character(dataset_pol$label))

#Balanceo con 32322  negativos y 43843  positivos
pos<-dataset_pol[dataset_pol$label=="positive",]
pos_data<-pos[sample(1:dim(pos)[1],size=32322,replace=F),]
dataset_pol<-rbind(dataset_pol[dataset_pol$label=="negative",],pos_data)
dataset_pol<-dataset_pol[order(as.numeric(rownames(dataset_pol))),]


pol_log<-Logistic(label~.,dataset_pol)

pol_log2<-glm(label~.,family=binomial(logit), data=dataset_pol)

pol_log3<-glm(label~.,family=binomial(logit), 
              data=dataset_pol[,c("earthPos","earthNeg","elhPos","elhNeg","label")])

pol_tree<-rpart(label~.,dataset_pol[,3:9])


log_pol<-mycv_part(10,dataset_pol[,c("earthPos","earthNeg","elhPos","elhNeg","label")]
                   ,"label",Logistic)
perf_log_pol<-conf_int_perf(log_pol)

log_pol<-mycv_part(10,dataset_pol,"label",Logistic)
perf_log_pol<-conf_int_perf(log_pol)



neu_info_gain<-information.gain(label~.,data=dataset_neu_samp)
pol_info_gain<-information.gain(label~.,data=dataset_pol)

