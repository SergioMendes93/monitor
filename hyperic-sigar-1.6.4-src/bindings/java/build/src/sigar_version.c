#include "sigar.h"

static sigar_version_t sigar_version = {
    "03/25/2017 01:58 PM",
    "exported",
    "1.6.4.0",
    "amd64-linux",
    "libsigar-amd64-linux.so",
    "sigar-amd64-linux",
    "SIGAR-1.6.4.0, "
    "SCM revision exported, "
    "built 03/25/2017 01:58 PM as libsigar-amd64-linux.so",
    1,
    6,
    4,
    0
};

SIGAR_DECLARE(sigar_version_t *) sigar_version_get(void)
{
    return &sigar_version;
}
