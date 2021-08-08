## The main idea of this solution:

- Use a crontab along with Redis CLI:

**Idea:**
First of all, we have to install easy_install OR via sudo:

```sh
sudo apt-get update -y && \
     apt-get upgrade -y && \
     apt-get autoremove --purge -y

sudo apt-get install -y supervisord
```

Or via easy_install:

```sh
easy_install supervisord
easy_install supervisor-stdout
```

Also we need to provide some privileges to the www-data user:

```sh
usermod -s /bin/bash www-data
```

Let's put new supervisord config into **/etc/supervisord/conf.d/**:

```sh
[program:cron]
command=cron -f -L 8
autostart=true
autorestart=true
```

Afterwards, we demand to create the crontab file:

```sh
0 0 */3 * * su www-data "flock -w 0 /var/www/<project_directory>/notifications_recheck.lock /var/www/<project_directory>/bin console --env=prod customers:notifications:recheck > /var/www/<project_directory>/var/logs/cron_notifications_recheck.log 2>&1"
0 0 */7 * * su www-data "flock -w 0 /var/www/<project_directory>/notifications_recheck.lock /var/www/<project_directory>/bin console --env=prod customers:notifications:recheck > /var/www/<project_directory>/var/logs/cron_notifications_recheck.log 2>&1"
0 0 */14 * * su www-data "flock -w 0 /var/www/<project_directory>/notifications_recheck.lock /var/www/<project_directory>/bin console --env=prod customers:notifications:recheck > /var/www/<project_directory>/var/logs/cron_notifications_recheck.log 2>&1"
0 0 */18 * * su www-data "flock -w 0 /var/www/<project_directory>/notifications_recheck.lock /var/www/<project_directory>/bin console --env=prod customers:notifications:recheck > /var/www/<project_directory>/var/logs/cron_notifications_recheck.log 2>&1"
```

Meanwhile, in the Redis we can define new customers with some UUID or ID, which will be rechecked out every minute with the CRON.
If key was already deleted, then the previous notifications scripts will be executed again, until this key will appear once more in the Redis Store.

**Addition:**

> We can actually change redis.conf, it will allow us to interact with Redis in LRU mode:

```sh
maxmemory 2mb
maxmemory-policy allkeys-lru
```

For instance, we can define this topology of customer's notifactions statuses:
If one of the status:<ID> will be not incremented in the list, we will already know, which
notifaction service will be executed for them:

```sh
redis> SET customer:notification:status:<ID> "true"
redis> EXPIRE customer:notification:status:<ID> 3600 (one hour)
redis> TTL customer:notification:status:<ID>
integer(3600)
redis> DEL customer:notification:status:<ID>
```

The main pros of this idea, that redis is laying under RAM and every our request will be not taking a plenty of time for condition checking.
