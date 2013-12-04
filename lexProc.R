lexicon <-read.csv("lexicon.csv",header=T,sep="\t")

polScorePos<-lexicon$polScore[lexicon$polScore>0]
rob.max<-quantile(polScorePos,0.98)

polScoreNeg<-lexicon$polScore[lexicon$polScore<0]
rob.min<-quantile(polScoreNeg,0.02)



polScoreNorm<-ifelse(lexicon$polScore>0,5*lexicon$polScore/rob.max, 
                     ifelse(lexicon$polScore<0,-5*lexicon$polScore/rob.min,0))

polScoreNormTruc<-ifelse(polScoreNorm>5,5,ifelse(polScoreNorm<(-5),-5,polScoreNorm))

polScoreNormTruc<-round(polScoreNormTruc,2)

                     
lexicon<-cbind(lexicon,polScoreNormTruc)   


subScorePos<-lexicon$subScore[lexicon$subScore>0]
rob.sub.max<-quantile(subScorePos,0.98)

subScoreNeg<-lexicon$subScore[lexicon$subScore<0]
rob.sub.min<-quantile(subScoreNeg,0.02)



subScoreNorm<-ifelse(lexicon$subScore>0,5*lexicon$subScore/rob.sub.max, 
                     ifelse(lexicon$subScore<0,-5*lexicon$subScore/rob.sub.min,0))

subScoreNormTruc<-ifelse(subScoreNorm>5,5,ifelse(subScoreNorm<(-5),-5,subScoreNorm))

subScoreNormTruc<-round(subScoreNormTruc,2)













write.csv(lexicon,"lexicon2.csv",sep="\t")
