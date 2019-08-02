# Image to run Citeplag Frontend in a single docker instance.
# A msql/maria db instance for the pmc oas files is still needed.
FROM php:5.6-fpm
LABEL maintainer="vincent.stange@uni-konstanz.de"

# Expose the port nginx is reachable on
EXPOSE 80

# We want to run php5 fpm plus a nginx to properly use it in a single instance
# Supervisor will execute both services properly
RUN apt-get update && apt-get install -y nginx supervisor curl nano \
    mysql-client \
    libmcrypt-dev \
    libxml2-dev \
    libcurl4-nss-dev \
    libc-client-dev

RUN docker-php-ext-install -j$(nproc) mcrypt \
    && docker-php-ext-install -j$(nproc) curl \
    && docker-php-ext-install mysql mysqli pdo pdo_mysql

# Configure nginx
COPY config/citeplag.conf /etc/nginx/conf.d/default.conf
RUN rm /etc/nginx/sites-enabled/default

# Configure PHP-FPM
COPY config/fpm-pool.conf /usr/local/etc/php-fpm.d/www.conf
#COPY config/php.ini /etc/php7/conf.d/zzz_custom.ini
COPY config/zzz_custom_php.ini /usr/local/etc/php/conf.d/custom_php.ini

# Configure supervisord
COPY config/supervisord.conf /etc/supervisor/conf.d/supervisord.conf

# Copy frontend files
COPY index.php  /citeplag/
COPY app /citeplag/app/
COPY lib /citeplag/lib/
RUN chmod -R 777 /citeplag/app/tmp/

# Let supervisord start nginx & php-fpm
CMD ["/usr/bin/supervisord", "-c", "/etc/supervisor/conf.d/supervisord.conf"]
