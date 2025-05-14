import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './danh-sach-bap-nuoc.reducer';

export const DanhSachBapNuoc = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const danhSachBapNuocList = useAppSelector(state => state.danhSachBapNuoc.entities);
  const loading = useAppSelector(state => state.danhSachBapNuoc.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="danh-sach-bap-nuoc-heading" data-cy="DanhSachBapNuocHeading">
        <Translate contentKey="projectMovieApp.danhSachBapNuoc.home.title">Danh Sach Bap Nuocs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="projectMovieApp.danhSachBapNuoc.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/danh-sach-bap-nuoc/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="projectMovieApp.danhSachBapNuoc.home.createLabel">Create new Danh Sach Bap Nuoc</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {danhSachBapNuocList && danhSachBapNuocList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="projectMovieApp.danhSachBapNuoc.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('soDienThoai')}>
                  <Translate contentKey="projectMovieApp.danhSachBapNuoc.soDienThoai">So Dien Thoai</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('soDienThoai')} />
                </th>
                <th className="hand" onClick={sort('tenBapNuoc')}>
                  <Translate contentKey="projectMovieApp.danhSachBapNuoc.tenBapNuoc">Ten Bap Nuoc</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tenBapNuoc')} />
                </th>
                <th>
                  <Translate contentKey="projectMovieApp.danhSachBapNuoc.ve">Ve</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {danhSachBapNuocList.map((danhSachBapNuoc, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/danh-sach-bap-nuoc/${danhSachBapNuoc.id}`} color="link" size="sm">
                      {danhSachBapNuoc.id}
                    </Button>
                  </td>
                  <td>{danhSachBapNuoc.soDienThoai}</td>
                  <td>{danhSachBapNuoc.tenBapNuoc}</td>
                  <td>{danhSachBapNuoc.ve ? <Link to={`/ve/${danhSachBapNuoc.ve.id}`}>{danhSachBapNuoc.ve.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        tag={Link}
                        to={`/danh-sach-bap-nuoc/${danhSachBapNuoc.id}`}
                        color="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/danh-sach-bap-nuoc/${danhSachBapNuoc.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/danh-sach-bap-nuoc/${danhSachBapNuoc.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="projectMovieApp.danhSachBapNuoc.home.notFound">No Danh Sach Bap Nuocs found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default DanhSachBapNuoc;
