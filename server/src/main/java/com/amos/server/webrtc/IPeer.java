package com.amos.server.webrtc;

import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;

public interface IPeer {

    public void setRemoteDescriptorPeer(SessionDescription descriptorPeer);
    public void setRemoteIceCandidate(IceCandidate candidate);

}
