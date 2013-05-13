class base {

  exec { "apt-get update":
       command    => "/usr/bin/apt-get update",
           onlyif => "/bin/sh -c '[ ! -f /var/cache/apt/pkgcache.bin ] || /usr/bin/find /etc/apt/* -cnewer /var/cache/apt/pkgcache.bin | /bin/grep . > /dev/null'",
  }

# install foundationdb stuff
# sudo dpkg -i foundationdb-clients_0.2.1-2_amd64.deb

  package { "maven2" :
    ensure  => present,
    require => Exec['apt-get update'],
  }

  package { "openjdk-7-jdk" :
    ensure  => present,
    require => Package['maven2'],
  }

  package { ["openjdk-6-jdk","openjdk-6-jre","openjdk-6-jre-headless","openjdk-6-jre-lib"] :
    ensure  => absent,
    require => Package['openjdk-7-jdk']
  }

}

node default {
  include base
}
