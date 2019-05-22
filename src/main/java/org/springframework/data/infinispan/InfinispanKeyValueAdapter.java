package org.springframework.data.infinispan;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.dsl.QueryBuilder;
import org.springframework.data.keyvalue.core.AbstractKeyValueAdapter;
import org.springframework.data.keyvalue.core.ForwardingCloseableIterator;
import org.springframework.data.keyvalue.core.query.KeyValueQuery;
import org.springframework.data.util.CloseableIterator;
import org.springframework.util.Assert;

/**
 * @author Katia Aresti
 */
public class InfinispanKeyValueAdapter extends AbstractKeyValueAdapter {

   private RemoteCacheManager remoteCacheManager;

   public InfinispanKeyValueAdapter(RemoteCacheManager remoteCacheManager) {
      super(new InfinispanQueryEngine());
      Assert.notNull(remoteCacheManager, "remoteCacheManager must not be 'null'.");
      this.remoteCacheManager = remoteCacheManager;
   }

   public void setRemoteCacheManager(RemoteCacheManager remoteCacheManager) {
      this.remoteCacheManager = remoteCacheManager;
   }

   @Override
   public <T> Iterable<T> find(KeyValueQuery<?> query, String keyspace, Class<T> type) {
      KeyValueQuery<?> keyValueQuery = query;
      if(query.getCriteria() == null) {
         QueryBuilder from = Search.getQueryFactory(remoteCacheManager.getCache(keyspace)).from(type);
         keyValueQuery = new KeyValueQuery<>(from);
         keyValueQuery.setSort(query.getSort());
         keyValueQuery.setRows(query.getRows());
         keyValueQuery.setOffset(query.getOffset());
      }
      return super.find(keyValueQuery, keyspace, type);
   }

   @Override
   public Object put(Object id, Object item, String cacheName) {
      return getCache(cacheName).put(id, item);
   }

   @Override
   public boolean contains(Object id, String keyspace) {
      return getCache(keyspace).containsKey(id);
   }

   @Override
   public Object get(Object id, String keyspace) {
      return getCache(keyspace).get(id);
   }

   @Override
   public Object delete(Object id, String keyspace) {
      return getCache(keyspace).remove(id);
   }

   @Override
   public Iterable<?> getAllOf(String keyspace) {
      return getCache(keyspace).values();
   }

   @Override
   public void deleteAllOf(String keyspace) {
      getCache(keyspace).clear();
   }

   @Override
   public void clear() {
      remoteCacheManager.getCacheNames().forEach(cacheName ->
            remoteCacheManager.getCache(cacheName).clear());
   }

   protected RemoteCache<Object, Object> getCache(String cacheName) {
      return remoteCacheManager.getCache(cacheName);
   }

   @Override
   public void destroy() {
      this.remoteCacheManager.stop();
   }

   @Override
   public long count(String keyspace) {
      return getCache(keyspace).size();
   }

   @Override
   public CloseableIterator<Entry<Object, Object>> entries(String keyspace) {
      Iterator<Entry<Object, Object>> iterator = this.getCache(keyspace).entrySet().iterator();
      return new ForwardingCloseableIterator<>(iterator);
   }
}
