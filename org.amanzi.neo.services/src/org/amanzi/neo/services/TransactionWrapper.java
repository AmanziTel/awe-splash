package org.amanzi.neo.services;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import org.amanzi.neo.db.manager.DatabaseManager;
import org.neo4j.graphdb.Transaction;

public class TransactionWrapper {
    private ExecutorService executor;
    private Thread thread;
    private org.neo4j.graphdb.Transaction tx;
    private boolean isChanged;
    
    public TransactionWrapper() {
        tx=null;
        executor = Executors.newSingleThreadExecutor();
        try {
            thread=executor.submit(new Callable<Thread>() {

                @Override
                public Thread call() throws Exception {
                    return Thread.currentThread();
                }
            }).get();
            beginTx();
        } catch (InterruptedException e) {
            // TODO Handle InterruptedException
            throw (RuntimeException) new RuntimeException( ).initCause( e );
        } catch (ExecutionException e) {
            // TODO Handle ExecutionException
            throw (RuntimeException) new RuntimeException( ).initCause( e );
        }
    }
    private void beginTx() throws InterruptedException, ExecutionException {
        if (tx==null){
            tx=executor.submit(new Callable<Transaction>() {

                @Override
                public Transaction call() throws Exception {
                    return DatabaseManager.getInstance().getCurrentDatabaseService().beginTx();
                }
            }).get();
            isChanged=false;
        }else{
            //TODO generate error?
        }
    }
    public void commit(){
        try {
            executor.submit(new Runnable() {
                
                @Override
                public void run() {
                    tx.success();
                    tx.finish();
                    tx=DatabaseManager.getInstance().getCurrentDatabaseService().beginTx();
                }
            }).get();
            isChanged=false;
        } catch (InterruptedException e) {
            // TODO Handle InterruptedException
            throw (RuntimeException) new RuntimeException( ).initCause( e );
        } catch (ExecutionException e) {
            // TODO Handle ExecutionException
            throw (RuntimeException) new RuntimeException( ).initCause( e );
        }      
    }
    public void rollback(){
        try {
            executor.submit(new Runnable() {
                
                @Override
                public void run() {
                    tx.failure();
                    tx.finish();
                    tx=DatabaseManager.getInstance().getCurrentDatabaseService().beginTx();
                }
            }).get();
            isChanged=false;
        } catch (InterruptedException e) {
            // TODO Handle InterruptedException
            throw (RuntimeException) new RuntimeException( ).initCause( e );
        } catch (ExecutionException e) {
            // TODO Handle ExecutionException
            throw (RuntimeException) new RuntimeException( ).initCause( e );
        }      
    }
    @Override
    protected void finalize() throws Throwable {
        if (tx!=null){
            stop(false);
        }
        super.finalize();
    }
    public <T> Future<T>  submit(Callable<T> task){
        isChanged=true;
        return executor.submit(task);
    }
    public Future<?> submit(Runnable task){
        isChanged=true;
        return executor.submit(task);
    }
    public  void stop(boolean isCommit) {
        try {
            finishTx(isCommit);
        } catch (InterruptedException e) {
            // TODO Handle InterruptedException
            throw (RuntimeException) new RuntimeException( ).initCause( e );
        } catch (ExecutionException e) {
            // TODO Handle ExecutionException
            throw (RuntimeException) new RuntimeException( ).initCause( e );
        }finally{
            tx=null;
            executor.shutdown();
        }
        
    }
    private void finishTx(final boolean isCommit) throws InterruptedException, ExecutionException {
        executor.submit(new Runnable() {
            
            @Override
            public void run() {
                if (isCommit){
                    tx.success();
                }
                tx.finish();
            }
        }).get();
        tx=null;
    }
    public static void main(String[] args) {
        ScheduledExecutorService shed = Executors.newSingleThreadScheduledExecutor();
        ScheduledExecutorService shed2 = Executors.newSingleThreadScheduledExecutor();
        Runnable command=new Runnable() {
            
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getId());
            }
        };
        Runnable command2=new Runnable() {
            
            @Override
            public void run() {
                System.err.println(Thread.currentThread().getId());
            }
        };
        shed.execute(command);
        shed2.execute(command2);
        shed.execute(command);
        shed2.execute(command2);
    }
    public boolean isChanged() {
        return isChanged;
    }
}
