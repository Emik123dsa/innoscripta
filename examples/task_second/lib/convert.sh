#!/usr/bin/env bash
set -e

IFS=$'\n'

function wildcard() {
    local MIME_TYPE= $1;
    local MIME_TYPES= ("jpg", "jpeg", "png");
    if [[ ! -z "$MIME_TYPE" && "${MIME_TYPES[@]}" =~ "${MIME_TYPE}" ]]; then
       return wildcard "*.$MIME_TYPE" 
    fi;
}

exec ls -la | awk '{print $9}' | xargs -n2 sh -i -c 'convert "$1" -quality <convert_percentage> <output_dir>/convert."$1"'