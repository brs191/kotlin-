package hclient

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class TestNonPS(
    @JsonProperty("default_sms_app") val default_sms_app: String,
    @JsonProperty("vers") val vers: String,
    @JsonProperty("imei") val imei: String,
    @JsonProperty("sms_port") val sms_port: String,
    @JsonProperty("imsi") val imsi: String,
    @JsonProperty("provisioning_version") val provisioning_version: String,
    @JsonProperty("token") var token: String,
    @JsonProperty("msisdn") var msisdn: String,
    @JsonProperty("client_version") val client_version: String,
    @JsonProperty("client_vendor") val client_vendor: String,
    @JsonProperty("terminal_vendor") val terminal_vendor: String,
    @JsonProperty("terminal_model") val terminal_model: String,
    @JsonProperty("terminal_sw_version") val terminal_sw_version: String,
    @JsonProperty("rcs_state") val rcs_state: String,
    @JsonProperty("rcs_profile") val rcs_profile: String,
    @JsonProperty("rcs_version") val rcs_version: String,
    @JsonProperty("app") val app: String
)