const { getToken } = require('./auths');

async function changeStatusAccepted(id, versionNumber) {
  try {
    const token = getToken();

    const response = await fetch(`${process.env.API_BASE_URL}/contact/${id}/accepted`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${token}`,
      },
      body: JSON.stringify({
        versionNumber,
      }),
    });

    if (!response.ok) {
      throw new Error(`Error changing contact status. Response status: ${response.status}`);
    }
  } catch (error) {
    throw new Error(`Error while changing contact status. Details: ${error.message}`);
  }
}

async function changeStatusTurnedDown(id, reasonRefusal, versionNumber) {
  try {
    const token = getToken();

    const response = await fetch(`${process.env.API_BASE_URL}/contact/${id}/turnedDown`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${token}`,
      },
      body: JSON.stringify({
        reasonRefusal,
        versionNumber,
      }),
    });

    if (!response.ok) {
      throw new Error(`Error changing contact status. Response status: ${response.status}`);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    throw new Error(`Error while changing contact status. Details: ${error.message}`);
  }
}

async function changeStatusAdmitted(id, meetingPlace, versionNumber) {
  try {
    const token = getToken();

    const response = await fetch(`${process.env.API_BASE_URL}/contact/${id}/admitted`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${token}`,
      },
      body: JSON.stringify({
        meetingPlace,
        versionNumber,
      }),
    });

    if (!response.ok) {
      throw new Error(`Error changing contact status. Response status: ${response.status}`);
    }

    const data = await response.json();
    return data;
  } catch (error) {
    throw new Error(`Error while changing contact status. Details: ${error.message}`);
  }
}

async function changeStatusUnsupervised(id, versionNumber) {
  try {
    const token = getToken();

    const response = await fetch(`${process.env.API_BASE_URL}/contact/${id}/unsupervised`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${token}`,
      },
      body: JSON.stringify({
        versionNumber,
      }),
    });

    if (!response.ok) {
      throw new Error(`Error changing contact status. Response status: ${response.status}`);
    }
  } catch (error) {
    throw new Error(`Error while changing contact status. Details: ${error.message}`);
  }
}

module.exports = {
  changeStatusAccepted,
  changeStatusTurnedDown,
  changeStatusAdmitted,
  changeStatusUnsupervised,
};
