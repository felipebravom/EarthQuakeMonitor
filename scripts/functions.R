#Entrena haciendo k-folds cross-validation
#data: el data frame, label: la variable objetivo en String, method: la funcion
# en ... deben ir los parámetros adicionales del método
# Devuelve el data.frame con una nueva columna que tiene los valores predichos
mycv<-function(k,data,label,method,...){
  results<-data.frame()
  splits <- runif(nrow(data))
  for(i in 1:k){
    test.idx <- (splits >= (i - 1) / k) & (splits < i / k)
    train.idx <- !test.idx
    test <- data[test.idx, , drop=FALSE]
    train <- data[train.idx, , drop=FALSE]
    fun <- method(as.formula(paste(label,"~.")),data=train,...)
    fitted<-cbind(test,predicted=predict(object=fun, newdata=test,type="c"))
    results<-rbind(results,fitted)
    
  }
  results<-results[order(as.numeric(rownames(results))),]
  results 
}

# Entrena usando cross-validation y además me retorna todas las particiones con sus predicciones
# Para poder calcular intervalos de confianza de los resultados
mycv_part<-function(k,data,label,method,...){
  #result_frame<-data.frame()
  
  results<-vector("list",k)
  
  splits <- runif(nrow(data))
  for(i in 1:k){
    test.idx <- (splits >= (i - 1) / k) & (splits < i / k)
    train.idx <- !test.idx
    test <- data[test.idx, , drop=FALSE]
    train <- data[train.idx, , drop=FALSE]
    fun <- method(as.formula(paste(label,"~.")),data=train,...)
    fitted<-cbind(test,predicted=predict(object=fun, newdata=test,type="c"))
    
    results[[i]]<-fitted    
    #results<-rbind(results,fitted)
    
  }
  #results<-results[order(as.numeric(rownames(results))),]
  results 
}

## Particiona un data.frame en k particiones
partition_set<-function(k,data){
  #result_frame<-data.frame()
  
  results<-vector("list",k)
  
  splits <- runif(nrow(data))
  for(i in 1:k){
    test.idx <- (splits >= (i - 1) / k) & (splits < i / k)
    test <- data[test.idx, , drop=FALSE]
    
    results[[i]]<-test        
  }
  results 
}

# Calcula las métricas Accuracy, Precision, Recall, F-Score
my_performance<-function(predicted,real){
  xTab=table(predicted,real)
  acc=sum(xTab[1,1],xTab[2,2])/sum(xTab)
  prec <- xTab[2,2]/sum(xTab[2,]) # Precision
  rec <- xTab[2,2]/sum(xTab[,2]) #  Recall
  f <- (2*prec*rec)/sum(prec,rec) # F
  list(confusion=xTab,perfomance=c(accuracy=acc,precision=prec,recall=rec,fscore=f))
}

# Recibe una lista de data frames asume que la clasificacion es binaria y que hay 
# Una columna llamada predicted y otra label
my_cv_performance<-function(results){
  xTab=table(results$predicted,results$label)
  acc=sum(xTab[1,1],xTab[2,2])/sum(xTab)
  prec <- xTab[2,2]/sum(xTab[2,]) # Precision
  rec <- xTab[2,2]/sum(xTab[,2]) #  Recall
  f <- (2*prec*rec)/sum(prec,rec) # F
  perfomance=c(accuracy=acc,precision=prec,recall=rec,fscore=f)
}

# Intervalo de confianza en formato String con signo mas menos
my_ci<-function (x, ci = 0.95) 
{
  a <- mean(x)
  s <- sd(x)
  n <- length(x)
  error <- qt(ci + (1 - ci)/2, df = n - 1) * s/sqrt(n)
  result<-paste(round(a,3),"±")
  result<-paste(result,round(error,2))
  return(result)
}

# Deja todos los resultados en un vector
conf_int_perf<-function(data){
  perf_vector<-lapply(data,my_cv_performance)  
  res<-perf_vector[[1]]
  for (i in 2:length(perf_vector)){
    res<-rbind(res,perf_vector[[i]])
  }
  rownames(res)<-NULL
  res<-as.data.frame(res)
  conf_values2<-apply(res,2,my_ci) 
}

