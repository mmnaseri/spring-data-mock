#!/usr/bin/env bash
#
# Deploy to sonatype by installing gnupg2, decrypting and installing a gpg secret key, and invoking mvn deploy.

die() {
  echo "$@" >&2
  exit 1
}

#######################################
# Check whether to exit gracefully because travis is not sending secret keys in environment variables.
# Globals:
#   TRAVIS_SECURE_ENV_VARS
# Arguments:
#   None
# Returns:
#   None
#######################################
checkTravisSecure() {
    if [[ $TRAVIS_SECURE_ENV_VARS != "true" ]]; then
        echo "no secure env vars available, skipping deployment"
        exit
    fi
}

#######################################
# Prints usage
# Globals:
#   None
# Arguments:
#   None
# Returns:
#   None
#######################################
usage() {
    echo "${0} gpg-decryption-hash gpg-key-file"
}

#######################################
# Given a |hash| and |name|, return the value of the encrypted value.
# Globals:
#   None
# Arguments:
#   HASH the hash provided when doing travis encrypt-file on the gpg2 key file
#   NAME the name of the encryption variable - either "key" or "iv"
# Returns:
#   The value of the encrypted variable
#######################################
encryption_value() {
    local -r HASH="$1"
    local -r NAME="$2"
    local -r ENCRYPTED_VARIABLE="encrypted_${HASH}_${NAME}"
    echo "${!ENCRYPTED_VARIABLE}"
}

#######################################
# Decrypts the keyfile, and imports the key to gpg2.
# Globals:
#   None
# Arguments:
#   KEY the key for decrypting
#   IV the iv for decrypting
#   ENC_KEY_FILE the encrypted key file
#   KEY_FILE the decrypted key file
# Returns:
#   None
#######################################
install_gpg_key() {
    local -r KEY="$1"
    local -r IV="$2"
    local -r ENC_KEY_FILE="$3"
    local -r KEY_FILE="$4"

    openssl aes-256-cbc -K "$KEY" -iv "$IV" -in "$ENC_KEY_FILE" -out "$KEY_FILE" -d
    gpg2 --batch --import "$KEY_FILE"
}

#######################################
# Installs the gnupg2 package
# Globals:
#   None
# Arguments:
#   None
# Returns:
#   None
#######################################
install_gpg() {
    sudo apt-get update -qq
    sudo apt-get install -y gnupg2
}

#######################################
# Deploys using mvn
# Globals:
#   None
# Arguments:
#   SETTINGS_FILE the settings file to provide to mvn for various settings and profiles
# Returns:
#   None
#######################################
mvn_deploy() {
    local -r SETTINGS_FILE="$1"
    # Allowing loopback entry on GnuPG 2.1+
    echo "allow-loopback-pinentry" > ~/.gnupg/gpg-agent.conf
    gpgconf --reload gpg-agent
    mvn -P release clean deploy -Drelease -s "$SETTINGS_FILE"
}

#######################################
# The main
# Globals:
#   None
# Arguments:
#   HASH the hash provided when doing travis encrypt-file on the gpg2 key file
#   ENC_GPG_KEY_FILE the encrypted gpg key file
#   MVN_SETTINGS_FILE the settings file for maven
# Returns:
#   None
#######################################
main() {
    checkTravisSecure

    if (($# < 3)); then
        die "$(usage)"
    fi
    local -r HASH="$1"
    local -r ENC_GPG_KEY_FILE="$2"
    local -r MVN_SETTINGS_FILE="$3"

    install_gpg
    install_gpg_key "$(encryption_value "$HASH" key)" "$(encryption_value "$HASH" iv)" "$ENC_GPG_KEY_FILE" "${ENC_GPG_KEY_FILE/.asc}"
    mvn_deploy "$MVN_SETTINGS_FILE"
}

# Run main if this is not included
if ((${#BASH_SOURCE[@]} == 1)); then
    main "$@"
fi
